package mutsa.common.domain.models.article;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.user.User;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "article")
public class Article extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
