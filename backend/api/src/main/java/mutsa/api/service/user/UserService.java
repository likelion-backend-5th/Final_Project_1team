package mutsa.api.service.user;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mutsa.api.config.jwt.JwtConfig;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.api.dto.auth.AccessTokenResponse;
import mutsa.api.dto.user.PasswordChangeDto;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.api.util.CookieUtil;
import mutsa.api.util.JwtTokenProvider;
import mutsa.api.util.JwtTokenProvider.JWTInfo;
import mutsa.common.domain.models.user.*;
import mutsa.common.dto.user.UserInfoDto;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.member.MemberRepository;
import mutsa.common.repository.redis.RefreshTokenRedisRepository;
import mutsa.common.repository.user.RoleRepository;
import mutsa.common.repository.user.UserRepository;
import mutsa.common.repository.user.UserRoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final JwtConfig jwtConfig;

    private final UserModuleService userModuleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

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

    public AccessTokenResponse validateRefreshTokenAndCreateAccessToken(
            String refreshToken,
            HttpServletRequest request
    ) {
        try {
            // JWT 토큰 검증 실패하면 JWTVerificationException 발생
            JWTInfo jwtInfo = JwtTokenProvider.decodeToken(jwtConfig.getEncodedSecretKey(), refreshToken);

            // 저장된 리프레시 토큰 가져오기
            String storedRefresh = refreshTokenRedisRepository.getRefreshToken(jwtInfo.getUsername())
                    .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            // 리프레시 토큰 비교
            if (!refreshToken.equals(storedRefresh)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            User user = fromJwtInfo(jwtInfo);

            List<String> authorities = user.getUserRoles().stream()
                    .flatMap(userRole -> userRole.getRole().getAuthorities().stream())
                    .map(Authority::getName)
                    .collect(Collectors.toList());

            String accessToken = JwtTokenProvider.createAccessToken(request, user.getUsername(),
                    jwtConfig.getRefreshTokenExpire(), jwtConfig.getEncodedSecretKey(),
                    authorities);

            return AccessTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();

        } catch (JWTVerificationException e) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

    }

//    public UserInfoDto findUserInfo(String username) {
//        return userRepository.findUserInfo(username);
//    }

    public UserInfoDto findUserInfo(String username) {
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

    private void isSameCurrentPassword(PasswordChangeDto passwordChangeDto, User findUser) {
        if (findUser.getPassword()
                .equals(encodedPassword(passwordChangeDto.getPassword()))) {
            throw new BusinessException(ErrorCode.DIFFERENT_PASSWORD);
        }
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