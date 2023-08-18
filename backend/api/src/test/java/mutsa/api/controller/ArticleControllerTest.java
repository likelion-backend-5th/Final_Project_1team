/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 11:40
 */

package mutsa.api.controller;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.service.article.ArticleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private List<Article> articles;

    @BeforeEach
    public void init() {
        user = userRepository.findByUsername("ArticleControllerTest")
                .orElse(null);

        if (user == null) {
            user = User.of(
                    "ArticleControllerTest",
                    passwordEncoder.encode("test"),
                    "articlecontrollertest@gmail.com",
                    null,
                    null
            );

            user = userRepository.save(user);
        }

        articles = articleRepository.findAll();

        if (articles == null || articles.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                Article article = Article.builder()
                        .title("title-" + (i+1))
                        .description("desc-" + (i+1))
                        .user(user)
                        .build();

                articles.add(article);
            }

            articles = articleRepository.saveAll(articles);
        }
    }

//    @Test
//    @DisplayName("게시글 목록 조회")
//    public void articlePage() {
//        mockMvc.perform()
//    }
}
