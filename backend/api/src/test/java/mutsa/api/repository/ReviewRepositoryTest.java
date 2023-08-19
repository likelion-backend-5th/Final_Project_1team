package mutsa.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import jakarta.transaction.Transactional;
import java.util.Optional;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.review.ReviewStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.review.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Mock
    private UserModuleService userModuleService;
    @Mock
    private ArticleModuleService articleModuleService;

    private User user;
    private Article article;

    @BeforeEach
    public void init() {
        user = User.of("user", "password", "email", "oauthName", null, null);
        when(userModuleService.getByApiId(any())).thenReturn(user);

        article = Article.builder()
            .apiId("test1234")
            .title("testTitle")
            .description("testDescription")
            .build();
        when(articleModuleService.getByApiId(any())).thenReturn(article);
    }

    @DisplayName("후기 등록")
    @Test
    public void createReview() {
        // given
        String content = "testContent";
        Integer point = 5;
        Review review = Review.of(user, article, content, point);

        // when
        review = reviewRepository.save(review);

        // then
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getPoint()).isEqualTo(point);
        assertThat(review.getReviewStatus()).isEqualTo(ReviewStatus.UPLOAD);
    }

    @DisplayName("후기 삭제")
    @Test
    public void deleteReview() {
        // given
        String content = "testContent";
        Integer point = 5;
        Review review = reviewRepository.save(Review.of(user, article, content, point));

        // when
        reviewRepository.delete(review);
        Optional<Review> deleted = reviewRepository.findByApiId(review.getApiId());

        // then
        assertThat(deleted.isPresent()).isFalse();
    }
}
