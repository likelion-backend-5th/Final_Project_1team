package mutsa.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mutsa.common.domain.models.user.User;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static mutsa.common.dto.constants.ImageConstants.DEFAULT_AVATAR_IMAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String apiId;
    private String nickname;
    private String image_url;
    private Set<String> role;
    private String zipcode;
    private String city;
    private String street;

    public static UserInfoDto fromEntity(User byUsername) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.username = byUsername.getUsername();
        userInfoDto.apiId = byUsername.getApiId();
        userInfoDto.nickname = byUsername.getUsername();
        userInfoDto.image_url = getImage(byUsername.getImageUrl());
        userInfoDto.zipcode = null;
        userInfoDto.city = null;
        userInfoDto.street = null;
        userInfoDto.role = byUsername.getUserRoles()
                .stream()
                .map(o -> o.getRole().getValue().name())
                .collect(Collectors.toSet());

        return userInfoDto;
    }

    private static String getImage(String imageUrl) {
        if (StringUtils.hasText(imageUrl)) {
            return imageUrl;
        }
        return DEFAULT_AVATAR_IMAGE;
    }
}