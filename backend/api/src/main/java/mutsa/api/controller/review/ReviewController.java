package mutsa.api.controller.review;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.review.ReviewDeleteDto;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewResponseDto;
import mutsa.api.service.review.ReviewService;
import mutsa.api.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            .body(reviewService.getReview(reviewApiId));
    }

    // 후기 전체 조회
    @GetMapping("/article/{articleApiId}/review")
    public ResponseEntity<Page<ReviewResponseDto>> getAllReview(
        @PathVariable("articleApiId") String articleApiId,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "limit", defaultValue = "5") Integer pageSize,
        @RequestParam(value = "sort", defaultValue = "descByDate") String sortType

    ) {
        return ResponseEntity
            .ok()
            .body(reviewService.findAllReview(articleApiId, pageNum, pageSize, sortType));
    }

    // 후기 수정
    @PutMapping("/review/{reviewApiId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
        @PathVariable("reviewApiId") String reviewApiId,
        @RequestBody ReviewRequestDto reviewUpdateDto
    ) {
        String username = SecurityUtil.getCurrentUsername();

        return ResponseEntity
            .ok()
            .body(reviewService.updateReview(reviewApiId, username, reviewUpdateDto));
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
