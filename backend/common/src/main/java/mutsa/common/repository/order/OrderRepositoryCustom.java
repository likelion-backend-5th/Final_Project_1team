package mutsa.common.repository.order;

import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderResponseDto> getOrderByFilterBySeller(OrderFilter orderFilter, User user, Pageable pageable);

    Page<OrderResponseDto> getOrderByFilterByConsumer(OrderFilter orderFilter, User user, Pageable pageable);
}
