/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:20
 */

package mutsa.api.dto.article;

import lombok.*;
import mutsa.api.dto.image.ImageResponseDto;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.article.ArticleStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto {
    private String title;
    private String description;
    private String username;
    private String thumbnail;
    private String apiId;
    private Status status;
    private ArticleStatus articleStatus;
    private String createdDate;
    private List<ImageResponseDto> images;
    private Long price;

    public static ArticleResponseDto to(Article entity) {
        return ArticleResponseDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .username(entity.getUser().getUsername())
                .thumbnail(entity.getThumbnail())
                .apiId(entity.getApiId())
                .status(entity.getStatus())
                .articleStatus(entity.getArticleStatus())
                .createdDate(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .price(entity.getPrice())
                .images(entity.getImages().stream().map(ImageResponseDto::to).toList())
                .build();
    }

    public void addArticleImages(List<ImageResponseDto> images) {
        this.images.addAll(images);
    }
}
