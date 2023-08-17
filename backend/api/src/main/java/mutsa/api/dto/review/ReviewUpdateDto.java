package mutsa.api.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateDto {

    private String content;
    private Integer point;
}
