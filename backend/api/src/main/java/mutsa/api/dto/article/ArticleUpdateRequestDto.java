/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 2:25
 */

package mutsa.api.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mutsa.api.dto.image.ImagesRequestDto;
import mutsa.common.domain.models.article.ArticleStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleUpdateRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String apiId;
    @NotNull
    private ArticleStatus articleStatus;
    private List<String> images;
}
