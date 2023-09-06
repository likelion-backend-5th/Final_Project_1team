package mutsa.api.service.user;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.common.domain.models.user.Member;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.UserRole;
import mutsa.common.repository.member.MemberRepository;
import mutsa.common.repository.user.RoleRepository;
import mutsa.common.repository.user.UserRoleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Qualifier("customOAuth2Service")
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private static final String ID_ATTRIBUTE = "id";
    private static final String LOGIN_ATTRIBUTE = "login";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String IMAGE_ATTRIBUTE = "image";
    private static final String LINK_ATTRIBUTE = "link";
    private static final String CREATE_FLAG = "create_flag";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService;

    /**
     * OAuth2 Code Grant 방식으로 인증을 진행하고, 인증이 완료되고 나서 Resource Server로 부터 유저 정보를 받아오면
     * OAuth2UserRequest에 담겨 있음. 해당 유저 정보가 DB에 없으면 회원가입을 진행하고 있으면 로그인을 진행.
     *
     * @param userRequest the user request
     * @return
     * @throws OAuth2AuthenticationExceptionx
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest)
            .getAttributes();

        String apiId = ((Integer) attributes.get(ID_ATTRIBUTE)).toString();
        String login = (String) attributes.get(LOGIN_ATTRIBUTE);
        String email = (String) attributes.get(EMAIL_ATTRIBUTE);

        String imageUrl = getImageUrl(attributes);
        HashMap<String, Object> necessaryAttributes = createAttributes(apiId, login,
            email, imageUrl);

        String username = email;
        Optional<User> user = userRepository.findByUsername(username);

        OAuth2User oAuth2User;
        if (user.isEmpty()) {
            oAuth2User = signUpUser(login, email, imageUrl, username,
                necessaryAttributes);
        } else {
            oAuth2User = updateUser(user.get(), necessaryAttributes);
        }

        return oAuth2User;
    }

    private static String getImageUrl(Map<String, Object> attributes) {
        String imageUrl = "";

        if (attributes.get(IMAGE_ATTRIBUTE) instanceof Map) {
            imageUrl =
                ((Map) (attributes.get(IMAGE_ATTRIBUTE))).get(LINK_ATTRIBUTE) == null ?
                    "" : (String) ((Map) (attributes.get(IMAGE_ATTRIBUTE))).get(LINK_ATTRIBUTE);
        }

        return imageUrl;
    }

    private HashMap<String, Object> createAttributes(String apiId, String login,
        String email, String imageUrl) {
        HashMap<String, Object> necessaryAttributes = new HashMap<>();
        necessaryAttributes.put(ID_ATTRIBUTE, apiId);
        necessaryAttributes.put(LOGIN_ATTRIBUTE, login);
        necessaryAttributes.put(EMAIL_ATTRIBUTE, email);
        necessaryAttributes.put("image_url", imageUrl);
        return necessaryAttributes;
    }

    //TODO 회원가입, 중복 회원가입 예외 처리 필요함
    private OAuth2User signUpUser(String login, String email, String imageUrl,
        String username, Map<String, Object> necessaryAttributes) {
        String encodedPassword = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());

        Member member = Member.of(login);
        memberRepository.save(member);

        User user = User.of(username, encodedPassword, email, login, imageUrl, member);
        Role role = roleRepository.findByValue(RoleStatus.ROLE_USER).orElseThrow(() ->
            new EntityNotFoundException(RoleStatus.ROLE_USER + "에 해당하는 Role이 없습니다."));
        UserRole userRole = UserRole.of(user, role);

        userRepository.save(user);
        userRoleRepository.save(userRole);
        necessaryAttributes.put(CREATE_FLAG, true);

        return CustomPrincipalDetails.of(user, necessaryAttributes);
    }

    private OAuth2User updateUser(User user, Map<String, Object> necessaryAttributes) {
        necessaryAttributes.put(CREATE_FLAG, false);
        return CustomPrincipalDetails.of(user, necessaryAttributes);
    }
}
