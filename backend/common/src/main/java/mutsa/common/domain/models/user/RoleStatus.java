package mutsa.common.domain.models.user;

import lombok.Getter;

@Getter
public enum RoleStatus {
    ROLE_USER, ROLE_ADMIN,
    ;

    public static RoleStatus of(String s) {
        return RoleStatus.valueOf(s.toUpperCase());
    }
}
