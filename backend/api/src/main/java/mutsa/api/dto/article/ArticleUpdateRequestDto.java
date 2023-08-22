/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 2:25
 */

package mutsa.api.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    private String username;
    @NotBlank
    private String apiId;
}
