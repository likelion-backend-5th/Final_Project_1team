package mutsa.common.domain.models.review;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;

import java.io.Serializable;
import java.util.UUID;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;

@Entity
@Table(name = "review")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String content;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
}
