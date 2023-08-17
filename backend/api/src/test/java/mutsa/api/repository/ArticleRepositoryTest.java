/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 12:52
 */

package mutsa.api.repository;

import jakarta.transaction.Transactional;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ArticleRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);
    private User user;

    @BeforeAll
    public void init() {
        user = User.of("user1", "1234", "user1@gmail.com", null, null, null);

        user = userRepository.save(user);
    }

    @Test
    public void ArticleEntityTestCase1() {
        Article article = Article.builder()
                .title("test Article 1")
                .description("test description 1")
                .user(user)
                .build();

        article = articleRepository.save(article);

        Article find = articleRepository.findById(article.getId()).orElse(null);

        log.info("{} {}", "article", article);
        assert find != null;
        log.info("{} {}", "find", find);

        Assertions.assertEquals(article, find);
    }
}
