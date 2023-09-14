package mutsa.api.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpOauthUserDto {
    private String phoneNumber;
    private String zipcode;
    private String city;
    private String street;
}

