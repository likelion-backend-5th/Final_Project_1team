/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 12:52
 */

package mutsa.api.repository;

import mutsa.api.dto.article.ArticleFilterDto;
import mutsa.common.domain.filter.article.ArticleFilter;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
public class ArticleRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);
    private User user;
    private List<Article> articles = new ArrayList<>();

    @BeforeEach
    public void init() {
        user = User.of("user1", "1234", "user1@gmail.com", null, null, null);

        user = userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Article article = Article.builder()
                    .title("article-" + (i + 1))
                    .description("desc-" + (i + 1))
                    .user(user)
                    .build();
            articles.add(article);
        }

        articles = articleRepository.saveAll(articles);
    }

    @Test
    @DisplayName("게시글 읽기 테스트")
    public void readAllByUserApiIdTest() {
        List<Article> entities = articleRepository.findAllByUser_username(user.getUsername());

        assert entities != null && !entities.isEmpty();
        Assertions.assertEquals(articles, entities);
    }

    @Test
    @DisplayName("유저가 올린 게시글 페이지네이션 테스트")
    public void readAllPageByUserApiIdTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        Page<Article> page = articleRepository.getPageByUsername(user.getUsername(), ArticleFilter.of(), pageable);

        assert page != null && !page.isEmpty();
        Assertions.assertEquals(articles, page.getContent());
    }

    @Test
    @DisplayName("모든 게시글 페이지네이션 테스트")
    public void readAllPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        Page<Article> page = articleRepository.getPage(ArticleFilter.of(), pageable);

        assert page != null && !page.isEmpty();
        Assertions.assertEquals(articles, page.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deleteArticleTest() {
        Article entity = articles.get(0);

        articleRepository.delete(entity);
        articles.remove(0);

        List<Article> entities = articleRepository.findAll();

        assert entities != null && !entities.isEmpty();
//        for (int i = 0; i < entities.size(); i++) {
//            if (entities.get(i).getStatus() == Status.DELETED) {
//                continue;
//            }
//        }
        Assertions.assertTrue(entities.containsAll(articles));
    }
}
