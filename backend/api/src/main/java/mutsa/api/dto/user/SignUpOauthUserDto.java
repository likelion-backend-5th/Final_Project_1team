package mutsa.api.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.embedded.Address;
import mutsa.common.domain.models.user.embedded.OAuth2Type;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpOauthUserDto {
    private String phoneNumber;
    private Address address;
}

