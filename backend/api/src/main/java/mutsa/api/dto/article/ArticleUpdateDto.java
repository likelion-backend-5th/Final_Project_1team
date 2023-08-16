/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 2:25
 */

package mutsa.api.dto.article;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ArticleUpdateDto {
    private String title;
    private String description;
    private String username;
    private String apiId;
}
