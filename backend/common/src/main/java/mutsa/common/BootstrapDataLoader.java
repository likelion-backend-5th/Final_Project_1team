package mutsa.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.domain.models.user.Authority;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.repository.member.MemberRepository;
import mutsa.common.repository.user.AuthorityRepository;
import mutsa.common.repository.user.RoleRepository;
import mutsa.common.repository.user.UserRepository;
import mutsa.common.repository.user.UserRoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BootstrapDataLoader {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createAdminUser() {
        createRoleAuthority();

        Map<String, Object> admin = new HashMap<>();
        admin.put("id", 1);
        admin.put("login", "admin");
        admin.put("email", "mutsaproject@gmail.com");
        admin.put("image_url", "");
        admin.put("role", RoleStatus.ROLE_ADMIN);

        loadUser(admin);
    }

    /**
     * Authority 규칙
     * 1. {1}.{2}
     * 2. {1}에는 api의 도메인을 의미
     * 3. {2}에는 사용되는 형태에 따라 작성
     */
    private void createRoleAuthority() {
        /**
         * new authority saves
         */
        log.info("------------------------ User Authority ------------------------");
        Authority createAuthority = saveAuthority("user.create");
        Authority updateAuthority = saveAuthority("user.update");
        Authority deleteAuthority = saveAuthority("user.delete");
        Authority readAuthority = saveAuthority("user.read");

        log.info("------------------------ Article Authority ------------------------");
        Authority createArticle = saveAuthority("article.create");
        Authority updateArticle = saveAuthority("article.update");
        Authority deleteArticle = saveAuthority("article.delete");
        Authority readArticle = saveAuthority("article.read");

        log.info("------------------------ order Authority ------------------------");
        Authority createOrder = saveAuthority("order.create");
        Authority updateOrder = saveAuthority("order.update");
        Authority deleteOrder = saveAuthority("order.delete");
        Authority readOrder = saveAuthority("order.read");

        log.info("------------------------ review Authority ------------------------");
        Authority createReview = saveAuthority("review.create");
        Authority updateReview = saveAuthority("review.update");
        Authority deleteReview = saveAuthority("review.delete");
        Authority readReview = saveAuthority("review.read");


        log.info("------------------------ report Authority ------------------------");
        Authority createReport = saveAuthority("report.create");
        Authority updateReport = saveAuthority("report.update");
        Authority deleteReport = saveAuthority("report.delete");
        Authority readReport = saveAuthority("report.read");

        Role userRole = saveRole(RoleStatus.ROLE_USER);
        Role adminRole = saveRole(RoleStatus.ROLE_ADMIN);

        userRole.getAuthorities().clear();
        userRole.addAuthorities(createAuthority, updateAuthority, deleteAuthority, readAuthority,
            createArticle, updateArticle, readArticle, deleteArticle,
            createReport, updateReport, deleteReport, readReport,
            createOrder, updateOrder, deleteOrder, readOrder,
            createReview, updateReview, deleteReview, readReview);

        adminRole.getAuthorities().clear();
        adminRole.addAuthorities(createAuthority, updateAuthority, deleteAuthority, readAuthority,
            createArticle, updateArticle, readArticle, deleteArticle,
            createReport, updateReport, deleteReport, readReport,
            createOrder, updateOrder, deleteOrder, readOrder,
            createReview, updateReview, deleteReview, readReview);

        roleRepository.saveAll(Arrays.asList(userRole, adminRole));


    }

    private void loadUser(Map<String, Object> attributes) {

    }

    private Authority saveAuthority(String name) {
        return authorityRepository.save(Authority.of(name));
    }

    private Role saveRole(RoleStatus roleStatus) {
        return roleRepository.save(Role.of(roleStatus));
    }
}
