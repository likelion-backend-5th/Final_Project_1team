package mutsa.api.service.review;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.review.ReviewDeleteDto;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.dto.review.ReviewUpdateDto;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.order.OrderModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewModuleService reviewModuleService;
    private final UserModuleService userModuleService;
    private final ArticleModuleService articleModuleService;
    private final OrderModuleService orderModuleService;

    // 리뷰 생성
    public ReviewResponseDto createReview(
        String articleApiId, String orderApiId, String username, ReviewRequestDto requestDto
        ) {
        User user = userModuleService.getByUsername(username);
        Article article = articleModuleService.getByApiId(articleApiId);
        Order order = orderModuleService.getByApiId(orderApiId);

        return reviewModuleService.createReview(article, order, user, requestDto);
    }

    // 리뷰 단일 조회 (모든 유저 접근 가능)
    public ReviewResponseDto findReview(String reviewApiId) {

        return reviewModuleService.getReview(reviewApiId);
    }

    // 전체 리뷰 조회 (모든 유저 접근 가능)
    public List<ReviewResponseDto> findAllReview(String articleApiId) {
        Article article = articleModuleService.getByApiId(articleApiId);

        return reviewModuleService.findAllReview(article);
    }

    // 리뷰 수정
    public ReviewResponseDto updateReview(String reviewApiId, String username, ReviewUpdateDto reviewUpdateDto) {
        User user = userModuleService.getByUsername(username);

        return reviewModuleService.updateReview(user, reviewApiId, reviewUpdateDto);
    }

    // 리뷰 삭제
    public ReviewDeleteDto deleteReview(String reviewApiId, String username) {
        User user = userModuleService.getByUsername(username);

        return reviewModuleService.deleteReview(user, reviewApiId);
    }
}
