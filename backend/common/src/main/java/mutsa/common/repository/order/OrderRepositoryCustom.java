package mutsa.common.repository.order;

import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getOrderByFilterBySeller(OrderFilter orderFilter, User user, Pageable pageable);

    Page<Order> getOrderByFilterByConsumer(OrderFilter orderFilter, User user, Pageable pageable);
}
