package mutsa.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mutsa.common.domain.models.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String apiId;
    private String nickname;
    private String image_url;
    private String zipcode;
    private String city;
    private String street;

    public static UserInfoDto fromEntity(User byUsername) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.username = byUsername.getUsername();
        userInfoDto.apiId = byUsername.getApiId();
        userInfoDto.nickname = byUsername.getUsername();
        userInfoDto.image_url = null;
        userInfoDto.zipcode = null;
        userInfoDto.city = null;
        userInfoDto.street = null;
        return userInfoDto;
    }
}