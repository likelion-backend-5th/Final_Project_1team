package mutsa.api.service.review;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewDeleteDto;
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
        // 유저 검증 : Order 작성자와 현재 요청자가 동일인물인지 검증
        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.REVIEW_PERMISSION_DENIED);
        }

        if (Objects.equals(order.getOrderStatus(), OrderStatus.END)) {
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
    // TODO 페이징 처리
    public List<ReviewResponseDto> findAllReview(Article article) {

        return article.getReviews().stream().map(ReviewResponseDto::fromEntity).collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(User user, String reviewApiId,
        ReviewUpdateDto reviewUpdateDto) {
        Review review = getByApiId(reviewApiId);
        // 유저 검증
        review.validUser(user);

        review.setContent(reviewUpdateDto.getContent());
        review.setPoint(reviewUpdateDto.getPoint());

        return ReviewResponseDto.fromEntity(reviewRepository.save(review));
    }

    // 리뷰 삭제
    @Transactional
    public ReviewDeleteDto deleteReview(User user, String reviewApiId) {
        Review review = getByApiId(reviewApiId);
        // 유저 검증
        review.validUser(user);

        reviewRepository.delete(review);

        ReviewDeleteDto dto = new ReviewDeleteDto();
        dto.setMessage("후기를 삭제했습니다.");

        return dto;
    }

    public Review getByApiId(String reviewApiId) {
        return reviewRepository.findByApiId(reviewApiId).orElseThrow(()->
            new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
