/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:20
 */

package mutsa.api.dto.article;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mutsa.common.domain.models.article.Article;

@Builder
@Getter
@Setter
public class ArticleResponseDto {
    private String title;
    private String description;
    private String username;
    private String thumbnail;
    private String apiId;

    public static ArticleResponseDto to(Article entity) {
        return ArticleResponseDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .username(entity.getUser().getUsername())
                .thumbnail(entity.getThumbnail())
                .apiId(entity.getApiId())
                .build();
    }
}
