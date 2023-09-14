/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:20
 */

package mutsa.api.dto.article;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mutsa.api.dto.image.ImagesRequestDto;

@Getter
@Setter
public class ArticleCreateRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private Long price;
    private List<ImagesRequestDto> images;
}
