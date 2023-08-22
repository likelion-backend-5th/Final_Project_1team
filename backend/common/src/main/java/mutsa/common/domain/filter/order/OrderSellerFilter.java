package mutsa.common.domain.filter.order;

import lombok.*;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSellerFilter {
    private OrderStatus orderStatus;
    private Article article;
    private Status status = Status.ACTIVE;

    public static OrderSellerFilter of(OrderStatus orderStatus, Article article) {
        OrderSellerFilter orderFilter = new OrderSellerFilter();
        orderFilter.orderStatus = orderStatus;
        orderFilter.article = article;
        return orderFilter;
    }
}
