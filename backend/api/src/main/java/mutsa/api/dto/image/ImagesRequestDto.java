/**
 * @project backend
 * @author ARA
 * @since 2023-09-01 AM 8:17
 */

package mutsa.api.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImagesRequestDto {
    private String s3URL;
    private String filename;
}
