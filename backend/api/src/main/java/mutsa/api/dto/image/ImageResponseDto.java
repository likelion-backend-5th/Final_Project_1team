/**
 * @project backend
 * @author ARA
 * @since 2023-09-01 AM 8:18
 */

package mutsa.api.dto.image;

import lombok.*;
import mutsa.common.domain.models.image.Image;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {
    private String apiId;
    private String refApiId;
    private String fullPath;

    public static ImageResponseDto to(Image entity) {
        return ImageResponseDto.builder()
                .apiId(entity.getApiId())
                .refApiId(entity.getRefApiId())
                .fullPath(entity.getPath() + "/" + entity.getFileName())
                .build();
    }

    public static ImageResponseDto to(String fullPath) {
        return ImageResponseDto.builder()
                .apiId("1234")
                .refApiId("1234")
                .fullPath(fullPath)
                .build();
    }

}
