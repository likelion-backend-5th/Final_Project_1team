package mutsa.common.domain.models.user;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.base.BaseTimeEntity;
import mutsa.common.domain.models.user.embedded.Address;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(schema = "user")
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private Address address;

    @Singular
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private final Set<UserRole> userRoles = new HashSet<>();
}
