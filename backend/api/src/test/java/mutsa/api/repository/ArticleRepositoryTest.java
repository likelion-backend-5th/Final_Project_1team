/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 12:52
 */

package mutsa.api.repository;

import mutsa.common.domain.filter.article.ArticleFilter;
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
import java.util.Collections;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
public class ArticleRepositoryTest {
    public final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
    private ArticleFilter articleFilter = ArticleFilter.of();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    private final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);
    private User user1, user2;
    private List<Article> articles = new ArrayList<>();

    @BeforeEach
    public void init() {
        user1 = User.of("ArticleRepositoryTestUser1", "1234", "user1@gmail.com", null, null, null);

        user1 = userRepository.save(user1);

        user2 = User.of("ArticleRepositoryTestUser2", "1234", "user2@gmail.com", null, null, null);

        user2 = userRepository.save(user2);

        for (int i = 0; i < 10; i++) {
            Article article = Article.builder()
                    .title("article-" + (i + 1))
                    .description("desc-" + (i + 1))
                    .user(user1)
                    .build();
            articles.add(article);
        }
        articles.get(0).setUser(user2);

        articles = articleRepository.saveAll(articles);
    }

    @Test
    @DisplayName("게시글 읽기 테스트")
    public void readByApiIdTest() {
        Article actual = articleRepository.findByApiId(articles.get(0).getApiId()).orElse(null);

        assert actual != null;
        Assertions.assertEquals(articles.get(0), actual);
    }

    @Test
    @DisplayName("유저가 올린 게시글 페이지네이션 테스트")
    public void readAllPageByUsernameTest() {
        Page<Article> page = articleRepository.getPageByUsername(user1.getUsername(), articleFilter, PAGEABLE);

        assert page != null && !page.isEmpty();
        Assertions.assertEquals(articles.size() - 1, page.getNumberOfElements());
        Assertions.assertNotEquals(-1, Collections.indexOfSubList(articles, page.getContent()));
    }

    @Test
    @DisplayName("모든 게시글 페이지네이션 테스트")
    public void readAllPage() {
        Page<Article> page = articleRepository.getPage(articleFilter, PAGEABLE);

        assert page != null && !page.isEmpty();
        Assertions.assertEquals(articles, page.getContent());
    }

    @Test
    @DisplayName("게시글 제목 기반 검색")
    public void readPageByTitle() {
        articleFilter.setTitle("article-1");

        Page<Article> page = articleRepository.getPage(articleFilter, PAGEABLE);

        assert page != null && !page.isEmpty();
        checkArticleEntity(articles.get(0), page.getContent().get(0));
    }

    @Test
    @DisplayName("게시글 내용 기반 검색")
    public void readPageByDescription() {
        articleFilter.setDescription("desc-1");

        Page<Article> page = articleRepository.getPage(articleFilter, PAGEABLE);

        assert page != null && !page.isEmpty();
        checkArticleEntity(articles.get(0), page.getContent().get(0));
    }

    @Test
    @DisplayName("게시글 유저이름 기반 검색")
    public void readPageByUsername() {
        articleFilter.setUsername(user2.getUsername());

        Page<Article> page = articleRepository.getPage(articleFilter, PAGEABLE);

        assert page != null && !page.isEmpty();
        checkArticleEntity(articles.get(0), page.getContent().get(0));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deleteArticleTest() {
        Article entity = articles.get(0);

        articleRepository.delete(entity);
        articles.remove(0);

        List<Article> entities = articleRepository.findAll();

        assert entities != null && !entities.isEmpty();
        Assertions.assertTrue(entities.containsAll(articles));
    }

    private void checkArticleEntity(Article expected, Article actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getApiId(), actual.getApiId());
        Assertions.assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
    }
}
