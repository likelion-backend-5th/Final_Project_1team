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
import mutsa.common.domain.models.image.Image;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static mutsa.common.dto.constants.ImageConstants.DEFAULT_ARTICLE_IMG;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto {
    public static final ImageResponseDto DEFAULT_ARTICLE_IMG_RESPONSE = ImageResponseDto.to(DEFAULT_ARTICLE_IMG);
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
                .createdDate(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss")))
                .price(entity.getPrice())
                .images(getImages(entity.getImages()))
                .build();
    }

    private static List<ImageResponseDto> getImages(List<Image> images) {
        if (images.size() == 0) {
            return List.of(DEFAULT_ARTICLE_IMG_RESPONSE);
        }
        return images.stream().map(ImageResponseDto::to).toList();
    }

    public void addArticleImages(List<ImageResponseDto> images) {
        this.images.addAll(images);
    }


}
