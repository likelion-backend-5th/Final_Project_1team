package mutsa.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String apiId;
    private String image_url;
    private String zipcode;
    private String city;
    private String street;
}
