package mutsa.common;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.Authority;
import mutsa.common.domain.models.user.Member;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.UserRole;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.member.MemberRepository;
import mutsa.common.repository.order.OrderRepository;
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
    private final ArticleRepository articleRepository;
    private final OrderRepository orderRepository;

    public void createAdminUser() {
        createRoleAuthority();

        Map<String, Object> admin = new HashMap<>();
        admin.put("id", 1);
        admin.put("login", "admin");
        admin.put("password", "admin1234");
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
        String apiId = ((Integer) attributes.get("id")).toString();
        String login = (String) attributes.get("login");
        String email = (String) attributes.get("email");
        String imageUrl = (String) attributes.get("image_url");
        String password = (String) attributes.get("password");

        RoleStatus role = (RoleStatus) attributes.get("role");
        HashMap<String, Object> necessaryAttributes = createNecessaryAttributes(apiId, login,
            email, imageUrl);

        String username = login;
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = signUpOrUpdateUser(login, email, imageUrl, username, password, userOptional,
            necessaryAttributes, role);
    }

    private User signUpOrUpdateUser(String login, String email, String imageUrl, String username, String password,
        Optional<User> userOptional, Map<String, Object> necessaryAttributes, RoleStatus roleEnum) {
        User user;
        //회원가입
        if (userOptional.isEmpty()) {
            Member member = Member.of(login);
            memberRepository.save(member);
            Role role = roleRepository.findByValue(roleEnum).orElseThrow(() ->
                new EntityNotFoundException(roleEnum + "에 해당하는 Role이 없습니다."));
            user = User.of(username, bCryptPasswordEncoder.encode(password), email, login, imageUrl, member);
            UserRole userRole = UserRole.of(user, role);

            userRepository.save(user);
            userRoleRepository.save(userRole);
            necessaryAttributes.put("create_flag", true);
        } else {
            user = userOptional.get();
            necessaryAttributes.put("create_flag", false);
        }
        return user;
    }

    private HashMap<String, Object> createNecessaryAttributes(String apiId, String login,
        String email, String imageUrl) {
        HashMap<String, Object> necessaryAttributes = new HashMap<>();
        necessaryAttributes.put("id", apiId);
        necessaryAttributes.put("login", login);
        necessaryAttributes.put("email", email);
        necessaryAttributes.put("image_url", imageUrl);
        return necessaryAttributes;
    }
    private Authority saveAuthority(String name) {
        return authorityRepository.save(Authority.of(name));
    }

    private Role saveRole(RoleStatus roleStatus) {
        return roleRepository.save(Role.of(roleStatus));
    }

    public void createAricleOrder() {
        User user1 = User.of(
                "ArticleControllerTestUser1",
                bCryptPasswordEncoder.encode("test"),
                "articlecontrollertestuser1@gmail.com",
                null,
                null,
                null
        );
        user1 = userRepository.save(user1);

        User user2 = User.of(
                "ArticleControllerTestUser2",
                bCryptPasswordEncoder.encode("test"),
                "articlecontrollertestuser2@gmail.com",
                null,
                null,
                null
        );
        user2 = userRepository.save(user2);

        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Article article = Article.builder()
                    .title("title-" + (i + 1))
                    .description("desc-" + (i + 1))
                    .user(i % 2 == 0 ? user1 : user2)
                    .build();

            articles.add(article);
        }

        articles = articleRepository.saveAll(articles);

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(orderRepository.save(Order.of(articles.get(i), i % 2 == 0 ? user1 : user2)));
        }
        orders = orderRepository.saveAll(orders);
    }
}
