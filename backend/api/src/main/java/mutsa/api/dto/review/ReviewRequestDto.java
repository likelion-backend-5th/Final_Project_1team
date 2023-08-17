package mutsa.api.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private String username;
    private String content;
    private String point;
}
