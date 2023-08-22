package mutsa.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String userId;
    private String accessToken;
}
