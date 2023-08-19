package mutsa.api.service.review;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.dto.review.ReviewUpdateDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.review.ReviewRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EntityManager entityManager;

    private User reviewer1, reviewer2, seller;
    private Article article;
    private Order order;

    @BeforeEach
    public void init() {
        reviewer1 = User.of("user1", "password", "email1@", "oauthName1", null, null);
        reviewer1 = userRepository.save(reviewer1);

        reviewer2 = User.of("user2", "password", "email2@", "oauthName2", null, null);
        reviewer2 = userRepository.save(reviewer2);

        seller = User.of("seller", "password", "sellerEmail@", "sellerOauthName", null, null);
        seller = userRepository.save(seller);

        article = Article.builder()
            .title("Pre Article 1")
            .description("Pre Article 1 desc")
            .user(seller)
            .build();
        article = articleRepository.save(article);

        order = Order.of(article, reviewer1);
        order.setOrderStatus(OrderStatus.END);
        order = orderRepository.save(order);
    }

    @DisplayName("후기 생성 서비스 테스트")
    @Test
    void createReview() {
        // given
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setContent("test Review");
        requestDto.setPoint(5);

        // when
        ReviewResponseDto responseDto
            = reviewService.createReview(article.getApiId(), order.getApiId(),
            reviewer1.getUsername(), requestDto);

        // then
        assertThat(responseDto.getUsername()).isEqualTo(reviewer1.getUsername());
    }

    @DisplayName("후기 단일 조회 서비스 테스트")
    @Test
    void findReview() {
        // given
        Review review = Review.of(reviewer1, article, "test Review", 5);
        review = reviewRepository.save(review);

        // when
        ReviewResponseDto responseDto = reviewService.findReview(review.getApiId());

        // then
        assertThat(responseDto.getContent()).isEqualTo(review.getContent());
        assertThat(responseDto.getPoint()).isEqualTo(review.getPoint());
        assertThat(responseDto.getUsername()).isEqualTo(reviewer1.getUsername());
    }

    @DisplayName("후기 전체 조회 서비스 테스트")
    @Test
    void findAllReview() {
        // given
        reviewRepository.save(Review.of(reviewer1, article, "content1", 1));
        reviewRepository.save(Review.of(reviewer2, article, "content2", 2));

        // when
        List<ReviewResponseDto> allReviews = reviewService.findAllReview(article.getApiId());

        // then
        log.info(allReviews.toString());
        assertThat(allReviews.size()).isEqualTo(2);
    }

    @DisplayName("후기 수정 서비스 테스트")
    @Test
    void updateReview() {
        // given
        Review review = Review.of(reviewer1, article, "test Review", 5);

        ReviewUpdateDto updateDto = new ReviewUpdateDto();
        updateDto.setContent("reviewUpdate test");
        updateDto.setPoint(3);

        // when
        ReviewResponseDto responseDto = reviewService.updateReview(review.getApiId(),
            reviewer1.getUsername(), updateDto);

        // then
        assertThat(updateDto.getContent()).isEqualTo(responseDto.getContent());
        assertThat(updateDto.getPoint()).isEqualTo(responseDto.getPoint());
    }

    @DisplayName("후기 삭제 서비스 테스트")
    @Test
    void deleteReview() {
        // given
        Review review = reviewRepository.save(Review.of(reviewer1, article, "content1", 1));
        entityManager.flush();
        entityManager.clear();

        // when
        reviewService.deleteReview(review.getApiId(), reviewer1.getUsername());
        entityManager.flush();
        entityManager.clear();

        // then
        Optional<Review> deletedReview = reviewRepository.findByApiId(review.getApiId());
        assertThat(deletedReview.isPresent()).isFalse();
    }
}

