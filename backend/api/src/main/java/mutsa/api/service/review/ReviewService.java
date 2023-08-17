package mutsa.api.service.review;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.review.ReviewStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.review.ReviewRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(()
            -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Review review = Review.builder()
            .content(requestDto.getContent())
            .point(Integer.parseInt(requestDto.getPoint()))
            .user(user)
            .status(ReviewStatus.UPDATE)
            .build();

        return ReviewResponseDto.fromEntity(reviewRepository.save(review));
    }

    // 리뷰 단일 조회
    public ReviewResponseDto getReview(String reviewApiId) {
        Review review = reviewRepository.findByApiId(reviewApiId).orElseThrow(()->
            new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        return ReviewResponseDto.fromEntity(review);
    }

    // TODO 전체 리뷰 조회

    // TODO 리뷰 수정

    // 리뷰 삭제
    public void deleteReview(String reviewApiId) {
        Review review = reviewRepository.findByApiId(reviewApiId).orElseThrow(()
            -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }
}
