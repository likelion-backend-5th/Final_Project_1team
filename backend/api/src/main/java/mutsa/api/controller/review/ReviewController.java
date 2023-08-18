package mutsa.api.controller.review;


import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.review.ReviewDeleteDto;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.dto.review.ReviewUpdateDto;
import mutsa.api.service.review.ReviewService;
import mutsa.api.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 후기 생성
    @PostMapping("/article/{articleApiId}/order/{orderApiId}/review")
    public ResponseEntity<ReviewResponseDto> createReview(
        @PathVariable("articleApiId") String articleApiId,
        @PathVariable("orderApiId") String orderApiId,
        @RequestBody ReviewRequestDto requestDto
    ) {
        String username = SecurityUtil.getCurrentUsername();
        ReviewResponseDto responseDto
            = reviewService.createReview(articleApiId, orderApiId, username, requestDto);

        return ResponseEntity
            .ok()
            .body(responseDto);
    }

    // 후기 단일 조회
    @GetMapping("/review/{reviewApiId}")
    public ResponseEntity<ReviewResponseDto> getReview(
        @PathVariable("reviewApiId") String reviewApiId
    ) {
        return ResponseEntity
            .ok()
            .body(reviewService.findReview(reviewApiId));
    }

    // 후기 전체 조회
    // TODO 페이징 처리
    @GetMapping("/article/{articleApiId}/review")
    public ResponseEntity<List<ReviewResponseDto>> getAllReview(
        @PathVariable("articleApiId") String articleApiId
    ) {
        return ResponseEntity
            .ok()
            .body(reviewService.findAllReview(articleApiId));
    }

    // 후기 수정
    @PutMapping("/review/{reviewApiId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
        @PathVariable("reviewApiId") String reviewApiId,
        @RequestBody ReviewUpdateDto updateDto
    ) {
        String username = SecurityUtil.getCurrentUsername();

        return ResponseEntity
            .ok()
            .body(reviewService.updateReview(reviewApiId, username, updateDto));
    }

    // 후기 삭제
    @DeleteMapping("/review/{reviewApiId}")
    public ResponseEntity<ReviewDeleteDto> deleteReview(
        @PathVariable("reviewApiId") String reviewApiId
    ) {
        String username = SecurityUtil.getCurrentUsername();

        return ResponseEntity
            .ok()
            .body(reviewService.deleteReview(reviewApiId, username));
    }
}
