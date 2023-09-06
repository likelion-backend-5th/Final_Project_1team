package mutsa.common.repository.user;

import mutsa.common.dto.user.UserInfoDto;

public interface CustomUserRepository {

    UserInfoDto findUserInfo(String username);
}
