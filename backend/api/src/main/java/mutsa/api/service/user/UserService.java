package mutsa.api.service.user;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.dto.LoginResponseDto;
import mutsa.api.dto.auth.AccessTokenResponse;
import mutsa.api.dto.auth.LoginRequest;
import mutsa.api.dto.user.PasswordChangeDto;
import mutsa.api.dto.user.SignUpOauthUserDto;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.api.util.CookieUtil;
import mutsa.api.util.JwtTokenProvider;
import mutsa.api.util.JwtTokenProvider.JWTInfo;
import mutsa.common.domain.models.user.*;
import mutsa.common.domain.models.user.embedded.Address;
import mutsa.common.domain.models.user.embedded.OAuth2Type;
import mutsa.common.dto.user.UserInfoDto;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.cache.UserCacheRepository;
import mutsa.common.repository.member.MemberRepository;
import mutsa.common.repository.redis.RefreshTokenRedisRepository;
import mutsa.common.repository.user.RoleRepository;
import mutsa.common.repository.user.UserRepository;
import mutsa.common.repository.user.UserRoleRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserModuleService userModuleService;
    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public void signUp(SignUpUserDto signUpUserDto) {
        Optional<User> user = userRepository.findByUsername(signUpUserDto.getUsername());
        if (user.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATION_USER);
        }
        signUpUserDto.setPassword(bCryptPasswordEncoder.encode(signUpUserDto.getPassword()));

        User newUser = SignUpUserDto.from(signUpUserDto);
        Member newMember = Member.of(signUpUserDto.getNickname());
        newUser.addMember(newMember);
        Role role = roleRepository.findByValue(RoleStatus.ROLE_USER)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ROLE));

        UserRole userRole = UserRole.of(newUser, role);
        userRole.addUser(newUser);

        userRepository.save(newUser);
        memberRepository.save(newMember);
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void signUp(SignUpUserDto signUpUserDto, String oauthName, String picture, OAuth2Type oAuth2Type) {
        //추후에 oauth전용으로 따로 만들어서 관리해야함
        Optional<User> user = userRepository.findByUsername(signUpUserDto.getUsername());
        if (user.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATION_USER);
        }
        signUpUserDto.setPassword(bCryptPasswordEncoder.encode(signUpUserDto.getPassword()));

        User newUser = SignUpUserDto.from(signUpUserDto, oauthName, picture, oAuth2Type);
        Member newMember = Member.of(signUpUserDto.getNickname());
        newUser.addMember(newMember);
        Role role = roleRepository.findByValue(RoleStatus.ROLE_USER)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ROLE));

        UserRole userRole = UserRole.of(newUser, role);
        userRole.addUser(newUser);

        userRepository.save(newUser);
        memberRepository.save(newMember);
        userRoleRepository.save(userRole);
    }


    public AccessTokenResponse validateRefreshTokenAndCreateAccessToken(
            String refreshToken,
            HttpServletRequest request
    ) {
        try {
            // JWT 토큰 검증 실패하면 JWTVerificationException 발생
            JWTInfo jwtInfo = jwtTokenProvider.decodeRefreshToken(refreshToken);

            // 저장된 리프레시 토큰 가져오기
            String storedRefresh = refreshTokenRedisRepository.getRefreshToken(jwtInfo.getUsername())
                    .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            // 리프레시 토큰 비교
            if (!refreshToken.equals(storedRefresh)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            User user = fromJwtInfo(jwtInfo);

            String accessToken = jwtTokenProvider.createAccessToken(request,
                    customUserDetailsService.loadUserByUsername(user.getUsername()));

            log.info("Access Token : {}", accessToken);

            return AccessTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();

        } catch (JWTVerificationException e) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

    }

    @Transactional
    public void signUpAuth(CustomPrincipalDetails customPrincipalDetails, SignUpOauthUserDto signupAuthUserDto) {
        User user = userModuleService.getByUsername(customPrincipalDetails.getUsername());
        Address address = Address.of(signupAuthUserDto.getZipcode(), signupAuthUserDto.getCity(), signupAuthUserDto.getStreet());
        user.updateAddress(address);
        //전화번호 추가
    }

//    public UserInfoDto findUserInfo(String username) {
//        UserInfoDto userInfo = userRepository.findUserInfo(username);
//        System.out.println(userInfo.toString());
//        return userInfo;
//    }

    public UserInfoDto findUserInfo(String username) {
        log.info("findUserInfo {}", username);
        User byUsername = userModuleService.getByUsername(username);
        return UserInfoDto.fromEntity(byUsername);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CustomPrincipalDetails user = (CustomPrincipalDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (user != null) {
            CookieUtil.removeCookie(request, response, CookieUtil.REFRESH_TOKEN);
            refreshTokenRedisRepository.removeRefreshToken(user.getUsername()); //저장소에서 삭제
        }
    }

    @Transactional
    public void changePassword(CustomPrincipalDetails user, PasswordChangeDto passwordChangeDto) {
        User findUser = findUsername(user.getUsername());

        isSameCurrentPassword(passwordChangeDto, findUser);
        if (!isSamePassword(passwordChangeDto.getNewPassword(),
                passwordChangeDto.getNewPasswordCheck())) {
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }

        if (isSamePassword(passwordChangeDto.getPassword(), passwordChangeDto.getNewPassword())) {
            throw new BusinessException(ErrorCode.SAME_PASSOWRD);
        }

        findUser.updatePassword(bCryptPasswordEncoder.encode(passwordChangeDto.getNewPassword()));
    }

    @Transactional
    public LoginResponseDto login(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            LoginRequest loginDto
    ) throws IOException {
        CustomPrincipalDetails principalDetails = customUserDetailsService.loadUserByUsername(loginDto.getUsername());
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), principalDetails.getPassword())) {
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }

        //로그인 시에 유저 캐시화
        userCacheRepository.setUser(userModuleService.getByUsername(loginDto.getUsername()));

        String accessToken = jwtTokenProvider.createAccessToken(httpServletRequest, principalDetails);

        if (!refreshTokenRedisRepository.getRefreshToken(loginDto.getUsername()).isPresent()) {
            String token = jwtTokenProvider.createRefreshToken(httpServletRequest, loginDto.getUsername());
            refreshTokenRedisRepository.setRefreshToken(loginDto.getUsername(), token);
        }
        String refreshToken = refreshTokenRedisRepository.getRefreshToken(loginDto.getUsername()).get();

        LoginResponseDto loginResponseDto = new LoginResponseDto(principalDetails.getUsername(), accessToken);

        ResponseCookie cookie = CookieUtil.createCookie(refreshToken);
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return loginResponseDto;
    }

    public String refreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(jwtTokenProvider::isCookieNameRefreshToken)
            .map(Cookie::getValue)
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_IN_COOKIE));
    }

    public boolean existUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void updateImageUrl(UserDetails userDetails,String raw) {
        User user = findUsername(userDetails.getUsername());

        user.updateImageUrl(raw.replace("\\", "").replace("\"", ""));
    }

    @Transactional
    public void updateEmail(UserDetails userDetails,String email) {
        User findUser = findUsername(userDetails.getUsername());

        findUser.updateEmail(email);
    }

    @Transactional
    public void updateAddress(UserDetails userDetails, Address address) {
        User findUser = findUsername(userDetails.getUsername());

        findUser.updateAddress(address);
    }

    private void isSameCurrentPassword(PasswordChangeDto passwordChangeDto, User findUser) {
        if (!findUser.getPassword()
                .equals(encodedPassword(passwordChangeDto.getPassword()))) {
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }
    }

    public boolean isOauthUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
            .getIsOAuth2();
    }

    private String encodedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private boolean isSamePassword(String password, String newPassword) {
        return password.equals(newPassword);
    }

    private User fromJwtInfo(JWTInfo jwtInfo) {
        return findUsername(jwtInfo.getUsername());
    }

    private User findUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}