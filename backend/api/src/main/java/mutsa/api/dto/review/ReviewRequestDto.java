package mutsa.api.dto.review;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mutsa.api.dto.image.ImagesRequestDto;

@Getter
@Setter
public class ReviewRequestDto {

    private String content;
    private Integer point;
    private List<ImagesRequestDto> images;
}
