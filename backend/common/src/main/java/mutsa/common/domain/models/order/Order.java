package mutsa.common.domain.models.order;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "`order`")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Builder.Default
    private final String apiId = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Order of(Article article, User user) {
        return Order.builder()
                .article(article)
                .user(user)
                .orderStatus(OrderStatus.PROGRESS) // 생성시에 진행중 표기
                .build();
    }

    public void validArticle(Article article) {
        if (this.article != article) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "url path가 부정확합니다.");
        }
    }

    public void validUser(User user) {
        if (this.user != user && this.article.getUser() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 부족한 유저가 확인하려 합니다.");
        }
    }
}
