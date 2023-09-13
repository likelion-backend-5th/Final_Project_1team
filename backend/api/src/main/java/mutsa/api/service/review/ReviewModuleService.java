package mutsa.api.service.review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewDeleteDto;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.review.ReviewStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.review.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewModuleService {

    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public Review createReview(Article article, Order order, User user, ReviewRequestDto requestDto) {
        // 유저 검증 : Order 작성자와 현재 요청자가 동일인물인지 검증
        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.REVIEW_PERMISSION_DENIED);
        }

        if (Objects.equals(order.getOrderStatus(), OrderStatus.END)) {
            return reviewRepository.save(
                Review.of(user, article, requestDto.getContent(), requestDto.getPoint()));
        }

        throw new BusinessException(ErrorCode.REVIEW_NOT_ALLOW);
    }

    // 리뷰 단일 조회 (모든 유저 접근 가능)
    public ReviewResponseDto getReview(String reviewApiId) {
        Review review = getByApiId(reviewApiId);

        return ReviewResponseDto.fromEntity(review);
    }

    // 전체 리뷰 조회 (모든 유저 접근 가능)
    public Page<ReviewResponseDto> findAllReview(Article article, int pageNum, int pageSize, String sortType) {

        Pageable pageable = switch (sortType) {
            case "descByDate" ->
                PageRequest.of(pageNum - 1, pageSize, Sort.by("createdAt").descending());
            case "ascByDate" ->
                PageRequest.of(pageNum - 1, pageSize, Sort.by("createdAt").ascending());
            case "descByPoint" ->
                PageRequest.of(pageNum - 1, pageSize, Sort.by("point").descending());
            case "ascByPoint" ->
                PageRequest.of(pageNum - 1, pageSize, Sort.by("point").ascending());
            default -> PageRequest.of(pageNum - 1, pageSize);
        };

        Page<Review> reviewPage = reviewRepository.findByArticle(article, pageable);

        return reviewPage.map(ReviewResponseDto::fromEntity);
    }

    // 리뷰 수정
    @Transactional
    public Review updateReview(User user, String reviewApiId,
        ReviewRequestDto reviewUpdateDto) {
        Review review = getByApiId(reviewApiId);
        // 유저 검증
        review.validUserById(user);

        review.setContent(reviewUpdateDto.getContent());
        review.setPoint(reviewUpdateDto.getPoint());
        review.setReviewStatus(ReviewStatus.UPDATED);

        return reviewRepository.save(review);
    }

    // 리뷰 삭제
    @Transactional
    public ReviewDeleteDto deleteReview(User user, String reviewApiId) {
        Review review = getByApiId(reviewApiId);
        // 유저 검증
        review.validUserById(user);

        reviewRepository.delete(review);


        ReviewDeleteDto dto = new ReviewDeleteDto();
        dto.setMessage("후기를 삭제했습니다.");

        return dto;
    }

    @Transactional
    public Review setImages(Review review, Collection<Image> images) {
        review.addImages(images);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review deleteImages(Review review) {
        review.setImages(new ArrayList<>());

        return reviewRepository.save(review);
    }

    public Review getByApiId(String reviewApiId) {
        return reviewRepository.findByApiId(reviewApiId).orElseThrow(()->
            new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
