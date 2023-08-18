package mutsa.common.domain.models.order;

import jakarta.persistence.*;
import lombok.*;
import mutsa.common.domain.models.BaseEntity;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;

import java.io.Serializable;
import java.util.Objects;
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
        Order order = Order.builder()
                .orderStatus(OrderStatus.PROGRESS) // 생성시에 진행중 표기
                .build();

        //연관관계 매핑
        order.setArticle(article);
        order.setUser(user);

        return order;
    }

    //연관관계 편의 메서드
    private void setUser(User user) {
        if (this.user != null) {
            user.getOrders().remove(this);
        }
        user.getOrders().add(this);
        this.user = user;
    }

    private void setArticle(Article article) {
        if (this.article != null) {
            article.getOrders().remove(this);
        }
        article.getOrders().add(this);
        this.article = article;
    }

    //유효성 검증 메서드
    public void validArticle(Article article) {
        if (!Objects.equals(this.article.getId(), article.getId())) {
            throw new BusinessException(ErrorCode.INVALID_ARTICLE_ORDER);
        }
    }

    public void validUser(User user) {
        if (!Objects.equals(this.user.getId(), user.getId()) && !Objects.equals(this.article.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.ORDER_PERMISSION_DENIED);
        }
    }

    // 리뷰 테스트 시 사용하기 위한 메소드
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
