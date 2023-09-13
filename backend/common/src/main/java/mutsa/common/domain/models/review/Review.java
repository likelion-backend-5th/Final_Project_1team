package mutsa.common.domain.models.review;

import static mutsa.common.domain.models.Status.ACTIVE;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;

import java.io.Serializable;
import java.util.UUID;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "review")
@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE `review` SET status = 'DELETED' WHERE review_id = ?")
@Where(clause = "status = 'ACTIVE'")
public class Review extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @Column(nullable = false,
        length = 500)
    private String content;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus reviewStatus;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = ACTIVE;

    @OneToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

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
            .reviewStatus(ReviewStatus.UPLOAD)
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

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    // 요청을 보낸 유저와 후기를 등록한 유저가 동일한지 검증
    public void validUserById(User user) {
        if (!Objects.equals(this.user.getId(), user.getId())) {
            throw new BusinessException(ErrorCode.REVIEW_PERMISSION_DENIED);
        }
    }

    public void addImages(Collection<Image> imageCollection) {
        this.images.addAll(imageCollection);
    }

    @Override
    public String toString() {
        return "Review{" +
            "id=" + id +
            ", apiId='" + apiId + '\'' +
            ", content='" + content + '\'' +
            ", point=" + point +
            ", reviewStatus=" + reviewStatus +
            '}';
    }
}
