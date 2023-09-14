package mutsa.common.domain.models.article;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.User;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "article")
@SQLDelete(sql = "UPDATE `article` SET status = 'DELETED', article_status = 'EXPIRED' WHERE article_id = ?")
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

    @Column(nullable = false)
    private String description;

    private String thumbnail;

    @Column(nullable = false)
    @Builder.Default
    private Long price = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ArticleStatus articleStatus = ArticleStatus.LIVE;

    @OneToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "article")
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "article")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void addImage(Image image) {this.images.add(image);}

    public void addImages(Collection<Image> imageCollections) {this.images.addAll(imageCollections);}

    public boolean validUser(User user) {
        return Objects.equals(this.user.getId(), user.getId());
    }
}
