package mutsa.common.domain.models.review;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "review")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false,
            columnDefinition = "text",
            length = 200)
    private String description;
}
