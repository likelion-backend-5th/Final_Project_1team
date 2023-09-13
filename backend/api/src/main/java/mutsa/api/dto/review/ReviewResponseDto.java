package mutsa.api.dto.review;

import static mutsa.common.dto.constants.ImageConstants.DEFAULT_ARTICLE_IMG;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mutsa.api.dto.image.ImageResponseDto;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.review.ReviewStatus;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {
    public static final ImageResponseDto DEFAULT_ARTICLE_IMG_RESPONSE = ImageResponseDto.to(DEFAULT_ARTICLE_IMG);
    private String apiId;
    private String content;
    private Integer point;
    private String username;
    private String createdAt;
    private List<ImageResponseDto> images;
    private ReviewStatus reviewStatus;

    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
            .apiId(review.getApiId())
            .content(review.getContent())
            .point(review.getPoint())
            .username(review.getUser().getUsername())
            .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .reviewStatus(review.getReviewStatus())
            .images(getImages(review.getImages()))
            .build();
    }

    private static List<ImageResponseDto> getImages(List<Image> images) {
        if (images.size() == 0) {
            return List.of(DEFAULT_ARTICLE_IMG_RESPONSE);
        }
        return images.stream().map(ImageResponseDto::to).toList();
    }

    @Override
    public String toString() {
        return "ReviewResponseDto{" +
            "apiId='" + apiId + '\'' +
            ", content='" + content + '\'' +
            ", point=" + point + '\'' +
            ", username='" + username + '\'' +
            ", reviewStatus=" + reviewStatus + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
