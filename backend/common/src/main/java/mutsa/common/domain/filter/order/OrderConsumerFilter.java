package mutsa.common.domain.filter.order;

import lombok.*;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.order.OrderStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderConsumerFilter {
    private OrderStatus orderStatus;
    private Status status = Status.ACTIVE;

    public static OrderConsumerFilter of(OrderStatus orderStatus) {
        OrderConsumerFilter orderFilter = new OrderConsumerFilter();
        orderFilter.orderStatus = orderStatus;
        return orderFilter;
    }
}
