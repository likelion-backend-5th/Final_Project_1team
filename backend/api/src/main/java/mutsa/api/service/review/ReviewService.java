package mutsa.api.service.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewDeleteDto;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.image.ImageModuleService;
import mutsa.api.service.order.OrderModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewModuleService reviewModuleService;
    private final UserModuleService userModuleService;
    private final ArticleModuleService articleModuleService;
    private final OrderModuleService orderModuleService;
    private final ImageModuleService imageModuleService;

    // 리뷰 생성
    public ReviewResponseDto createReview(
        String articleApiId, String orderApiId, String username, ReviewRequestDto requestDto
        ) {
        User user = userModuleService.getByUsername(username);
        Article article = articleModuleService.getByApiId(articleApiId);
        Order order = orderModuleService.getByApiId(orderApiId);

        Review review = reviewModuleService.createReview(article, order, user, requestDto);

        if (requestDto.getImages() == null || requestDto.getImages().isEmpty()) {
            return ReviewResponseDto.fromEntity(review);
        }

        List<Image> images = imageModuleService.saveAllReviewImage(requestDto.getImages(),
            review.getApiId());
        return ReviewResponseDto.fromEntity(reviewModuleService.setImages(review, images));
    }

    // 리뷰 단일 조회 (모든 유저 접근 가능)
    public ReviewResponseDto getReview(String reviewApiId) {

        return reviewModuleService.getReview(reviewApiId);
    }

    // 전체 리뷰 조회 (모든 유저 접근 가능)
    public Page<ReviewResponseDto> findAllReview(String articleApiId, int pageNum, int pageSize, String sortType) {
        Article article = articleModuleService.getByApiId(articleApiId);

        return reviewModuleService.findAllReview(article, pageNum, pageSize, sortType);
    }

    // 리뷰 수정
    public ReviewResponseDto updateReview(String reviewApiId, String username, ReviewRequestDto reviewUpdateDto) {
        User user = userModuleService.getByUsername(username);

        Review review = reviewModuleService.updateReview(user, reviewApiId, reviewUpdateDto);
        if (reviewUpdateDto.getImages() == null || reviewUpdateDto.getImages().isEmpty()) {
            return ReviewResponseDto.fromEntity(review);
        }

        imageModuleService.deleteByRefApiId(review.getApiId());
        review = deleteImages(review);
        List<Image> images = imageModuleService.saveAllReviewImage(reviewUpdateDto.getImages(), reviewApiId);
        return ReviewResponseDto.fromEntity(reviewModuleService.setImages(review, images));
    }

    // 리뷰 삭제
    public ReviewDeleteDto deleteReview(String reviewApiId, String username) {
        User user = userModuleService.getByUsername(username);
        imageModuleService.deleteAllByRefId(reviewApiId);

        return reviewModuleService.deleteReview(user, reviewApiId);
    }

    private Review deleteImages(Review review) {
        return reviewModuleService.deleteImages(review);
    }
}
