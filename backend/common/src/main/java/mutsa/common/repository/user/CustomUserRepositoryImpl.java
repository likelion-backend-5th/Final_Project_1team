package mutsa.common.repository.user;


import static mutsa.common.domain.models.user.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import mutsa.common.customRepository.Querydsl4RepositorySupport;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.user.UserInfoDto;
import org.springframework.util.StringUtils;

public class CustomUserRepositoryImpl extends Querydsl4RepositorySupport implements
    CustomUserRepository {
    public CustomUserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public UserInfoDto findUserInfo(String username) {
        return select(
            Projections.constructor(UserInfoDto.class,
                user.username,
                user.apiId,
                user.imageUrl,
                user.address.zipcode,
                user.address.city,
                user.address.street))
            .from(user)
            .where(username(username))
            .fetchOne();
    }

    private static BooleanExpression username(String username) {
        return StringUtils.hasText(username) ? user.username.eq(username): null;
    }
}
