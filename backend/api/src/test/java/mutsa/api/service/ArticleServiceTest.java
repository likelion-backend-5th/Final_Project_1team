/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:30
 */

package mutsa.api.service;

import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleFilterDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.service.article.ArticleService;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.article.ArticleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
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
    @DisplayName("Article Service 생성 및 저장 테스트")
    public void saveTest() {
        ArticleCreateRequestDto requestDto = new ArticleCreateRequestDto();

        requestDto.setTitle("Article1");
        requestDto.setDescription("Article1 Desc");
        requestDto.setUsername("user1");

        ArticleResponseDto responseDto = articleService.save(requestDto);

        assert responseDto != null;
        Assertions.assertEquals(requestDto.getTitle(), responseDto.getTitle());
        Assertions.assertEquals(requestDto.getDescription(), responseDto.getDescription());
        Assertions.assertEquals(requestDto.getUsername(), responseDto.getUsername());
    }

    @Test
    @DisplayName("Article Service 수정 테스트")
    public void updateTest() {
        ArticleUpdateRequestDto updateDto = new ArticleUpdateRequestDto(
                "[Updated]Pre Article 1",
                "[Updated]Pre Article 1 desc",
                "user1",
                articles.get(0).getApiId()
        );

        ArticleResponseDto responseDto = articleService.updateTest(updateDto);

        assert responseDto != null;
        Assertions.assertEquals(updateDto.getTitle(), responseDto.getTitle());
        Assertions.assertEquals(updateDto.getDescription(), responseDto.getDescription());
        Assertions.assertEquals(updateDto.getUsername(), responseDto.getUsername());
    }

    @Test
    @DisplayName("Article Service 유저 이름 기반 페이지네이션 테스트")
    public void pageByUsernameTest() {
        String username = user.getUsername();

        List<ArticleResponseDto> dtos = articleService.getPageByUsername(
                username,
                Sort.Direction.ASC,
                ArticleFilterDto.of()
        );

        assert dtos != null && !dtos.isEmpty();
        for (int i = 0; i < articles.size(); i++) {
            checkSameValue(articles.get(i), dtos.get(i));
        }
    }

    @Test
    @DisplayName("Article Service 페이지네이션 테스트")
    public void pageTest() {
        List<ArticleResponseDto> dtos = articleService.getPage(0, 10, Sort.Direction.ASC, ArticleFilterDto.of());

        assert dtos != null && !dtos.isEmpty();
        for (int i = 0; i < articles.size(); i++) {
            checkSameValue(articles.get(i), dtos.get(i));
        }
    }

    @Test
    @DisplayName("Article Service 필터 테스트 - 게시글 상태가 LIVE 인 경우만 페이징")
    public void filterTest() {
        articles.get(0).setArticleStatus(ArticleStatus.EXPIRED);

        articles = articleRepository.saveAll(articles);

        List<ArticleResponseDto> dtos = articleService.getPage(0, 10, Sort.Direction.ASC, ArticleFilterDto.of());

        assert dtos != null && !dtos.isEmpty();
        for (int i = 1; i < dtos.size(); i++) {
            checkSameValue(articles.get(i), dtos.get(i));
        }
    }

    @Test
    @DisplayName("Article Service 필터 테스트 - 게시글 상태가 EXPIRED 인 경우만 페이징")
    public void filterTest2() {
        articles.get(0).setArticleStatus(ArticleStatus.EXPIRED);

        articles = articleRepository.saveAll(articles);

        List<ArticleResponseDto> dtos = articleService.getPage(
                0,
                10,
                Sort.Direction.ASC,
                ArticleFilterDto.of(Status.ACTIVE, ArticleStatus.EXPIRED)
        );

        assert dtos != null && !dtos.isEmpty();
        checkSameValue(articles.get(0), dtos.get(0));
    }

    @Test
    @DisplayName("Article Service 필터 테스트 - 삭제되고 게시글 상태가 EXPIRED 인 경우만 페이징")
    public void filterTest3() {
        articleRepository.delete(articles.get(0));

        articles = articleRepository.findAll();

        List<ArticleResponseDto> dtos = articleService.getPage(
                0,
                10,
                Sort.Direction.ASC,
                ArticleFilterDto.of(Status.DELETED, ArticleStatus.EXPIRED)
        );

        assert dtos != null && !dtos.isEmpty();
        checkSameValue(articles.get(0), dtos.get(0));
    }

    private void checkSameValue(Article articleEntity, ArticleResponseDto articleResponseDto) {
        Assertions.assertEquals(articleEntity.getApiId(), articleResponseDto.getApiId());
        Assertions.assertEquals(articleEntity.getTitle(), articleResponseDto.getTitle());
        Assertions.assertEquals(articleEntity.getDescription(), articleResponseDto.getDescription());
        Assertions.assertEquals(articleEntity.getUser().getUsername(), articleResponseDto.getUsername());
        Assertions.assertEquals(articleEntity.getThumbnail(), articleResponseDto.getThumbnail());
        Assertions.assertEquals(articleEntity.getStatus(), articleResponseDto.getStatus());
        Assertions.assertEquals(articleEntity.getArticleStatus(), articleResponseDto.getArticleStatus());
    }
}
