/**
 * @project backend
 * @author ARA
 * @since 2023-09-01 AM 8:18
 */

package mutsa.api.dto.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageResponseDto {
    private String apiId;
    private String refApiId;
    private String path;
    private String fileName;
    private String userNickname;
}
