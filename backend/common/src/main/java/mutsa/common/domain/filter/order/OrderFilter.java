package mutsa.common.domain.filter.order;

import lombok.*;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.order.OrderStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFilter {
    public enum OrderUserType{
        SELLER, CONSUMER
    }

    private OrderUserType orderUserType;
    private OrderStatus orderStatus;;
    private Status status = Status.ACTIVE;
    private String text;

    public static OrderFilter of(String userType, String orderStatus, String text) {
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.orderUserType = OrderUserType.valueOf(userType);
        orderFilter.orderStatus = OrderStatus.of(orderStatus);
        orderFilter.text = text;
        return orderFilter;
    }
}
