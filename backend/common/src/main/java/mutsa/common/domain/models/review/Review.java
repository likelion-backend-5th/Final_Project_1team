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

    public static Review of(User user, Article article, String content, Integer point) {
        Review review = Review.builder()
            .content(content)
            .point(point)
            .status(ReviewStatus.UPLOAD)
            .build();

        review.setUser(user);
        review.setArticle(article);

        return review;
    }

    private void setUser(User user) {
        if (this.user != null) {
            user.getReviews().remove(this);
        }
        user.getReviews().add(this);
        this.user = user;
    }

    private void setArticle(Article article) {
        if (this.article != null) {
            article.getReviews().remove(this);
        }
        article.getReviews().add(this);
        this.article = article;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + id +
            ", apiId='" + apiId + '\'' +
            ", content='" + content + '\'' +
            ", point=" + point +
            ", status=" + status +
            '}';
    }
}
