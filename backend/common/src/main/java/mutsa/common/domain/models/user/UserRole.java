package mutsa.common.domain.models.user;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.base.BaseTimeEntity;

import java.io.Serializable;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@Table(schema = "user_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRole extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public void addUser(User user) {
        user.getUserRoles().add(this);
        this.user = user;
    }

    public static UserRole of(User user, Role role) {
        return UserRole.builder()
                .user(user)
                .role(role)
                .build();
    }
}
