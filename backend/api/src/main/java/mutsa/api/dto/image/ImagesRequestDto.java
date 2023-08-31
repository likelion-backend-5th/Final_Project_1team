/**
 * @project backend
 * @author ARA
 * @since 2023-09-01 AM 8:17
 */

package mutsa.api.dto.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ImagesRequestDto {
    private String refApiId;
    private List<String> s3URLs;
}
