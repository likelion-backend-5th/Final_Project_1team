package mutsa.common.domain.models.user;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.base.BaseTimeEntity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@Table(schema = "role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private final String apiId = UUID.randomUUID().toString();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoleStatus value;

    @ManyToMany
    @JoinTable(name = "role_authority",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "authority_id", nullable = false)})
    private final Set<Authority> authorities = new HashSet<>();

    public static Role of(RoleStatus value) {
        return Role.builder()
                .value(value)
                .build();
    }

    public void addAuthorities(Authority... authorities) {
        this.authorities.addAll(Arrays.stream(authorities).collect(Collectors.toSet()));
    }
}
