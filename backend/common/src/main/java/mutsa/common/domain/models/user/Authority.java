package mutsa.common.domain.models.user;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseTimeEntity;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Builder.Default
    @Column(nullable = false, updatable = false, unique = true)
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false)
    public String name;

    public static Authority of(String name) {
        return Authority.builder()
                .name(name)
                .build();
    }
}
