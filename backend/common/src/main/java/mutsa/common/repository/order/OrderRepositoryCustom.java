package mutsa.common.repository.order;

import mutsa.common.domain.filter.order.OrderConsumerFilter;
import mutsa.common.domain.filter.order.OrderSellerFilter;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getOrderByFilterBySeller(OrderSellerFilter orderFilter, Pageable pageable);

    Page<Order> getOrderByFilterByConsumer(OrderConsumerFilter orderFilter, User user, Pageable pageable);
}
