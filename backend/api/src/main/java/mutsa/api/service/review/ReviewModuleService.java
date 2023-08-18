package mutsa.api.service.review;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.dto.review.ReviewUpdateDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.review.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewModuleService {

    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public ReviewResponseDto createReview(Article article, Order order, User user, ReviewRequestDto requestDto) {
        // TODO 유저 검증
        if (order.getOrderStatus().equals(OrderStatus.END)) {
            return ReviewResponseDto.fromEntity(reviewRepository.save(
                Review.of(user, article, requestDto.getContent(), requestDto.getPoint()))
            );
        }

        throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOW);
    }

    // 리뷰 단일 조회 (모든 유저 접근 가능)
    public ReviewResponseDto getReview(String reviewApiId) {
        Review review = getByApiId(reviewApiId);

        return ReviewResponseDto.fromEntity(review);
    }

    // 전체 리뷰 조회 (모든 유저 접근 가능)
    public List<ReviewResponseDto> findAllReview(Article article) {

        return article.getReviews().stream().map(ReviewResponseDto::fromEntity).collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(User user, String reviewApiId,
        ReviewUpdateDto reviewUpdateDto) {
        // TODO 유저 검증
        Review review = getByApiId(reviewApiId);
        review.setContent(reviewUpdateDto.getContent());
        review.setPoint(reviewUpdateDto.getPoint());

        return ReviewResponseDto.fromEntity(reviewRepository.save(review));
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(User user, String reviewApiId) {
        // TODO 유저 검증
        Review review = getByApiId(reviewApiId);

        reviewRepository.delete(review);
    }

    public Review getByApiId(String reviewApiId) {
        return reviewRepository.findByApiId(reviewApiId).orElseThrow(()->
            new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
