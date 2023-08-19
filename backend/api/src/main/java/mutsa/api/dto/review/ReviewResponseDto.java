package mutsa.api.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.review.ReviewStatus;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {
    private String apiId;
    private String content;
    private Integer point;
    private String username;
    private ReviewStatus reviewStatus;

    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
            .apiId(review.getApiId())
            .content(review.getContent())
            .point(review.getPoint())
            .username(review.getUser().getUsername())
            .reviewStatus(review.getReviewStatus())
            .build();
    }

    @Override
    public String toString() {
        return "ReviewResponseDto{" +
            "apiId='" + apiId + '\'' +
            ", content='" + content + '\'' +
            ", point=" + point +
            ", username='" + username + '\'' +
            ", reviewStatus=" + reviewStatus +
            '}';
    }
}
