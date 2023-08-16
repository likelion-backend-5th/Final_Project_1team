/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:30
 */

package mutsa.api.service;

import jakarta.transaction.Transactional;
import mutsa.api.dto.article.ArticleRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateDto;
import mutsa.api.service.auth.article.ArticleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ArticleServiceTest {
    @Autowired
    ArticleService articleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    User user;
    Article preArticle;

    @BeforeEach
    public void init() {
        user = User.of("user1", "1234", "user1@gmail.com", null, null);

        user = userRepository.save(user);

        preArticle = Article.builder()
                .title("Pre Article 1")
                .description("Pre Article 1 desc")
                .user(user)
                .build();

        preArticle = articleRepository.save(preArticle);
    }

    @Test
    public void saveTest() {
        ArticleRequestDto requestDto = new ArticleRequestDto();

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
    public void updateTest() {
        ArticleUpdateDto updateDto = new ArticleUpdateDto(
                "[Updated]Pre Article 1",
                "[Updated]Pre Article 1 desc",
                "user1",
                preArticle.getApiId()
        );

        ArticleResponseDto responseDto = articleService.update(updateDto);

        assert responseDto != null;
        Assertions.assertEquals(updateDto.getTitle(), responseDto.getTitle());
        Assertions.assertEquals(updateDto.getDescription(), responseDto.getDescription());
        Assertions.assertEquals(updateDto.getUsername(), responseDto.getUsername());
    }
}
