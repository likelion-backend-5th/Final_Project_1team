package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static mutsa.common.exception.ErrorCode.ORDER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderModuleService {
    private final OrderRepository orderRepository;

    public OrderDetailResponseDto findDetailOrder(Article article, User user, String orderApiId) {
        Order order = getByApiId(orderApiId);

        order.validArticle(article);
        order.validUser(user); //판매자와 구매자만 확인할 수 있도록
        return OrderDetailResponseDto.fromEntity(order);
    }

    public List<OrderResponseDto> findAllOrder(Article article, User user) {
        article.validUser(user); //판매자만 확인할 수 있는지
        return article.getOrders().stream().map(OrderResponseDto::fromEntity).collect(Collectors.toList());
    }

    public OrderDetailResponseDto saveOrder(Article article, User user) {
        return OrderDetailResponseDto.fromEntity(orderRepository.save(Order.of(article, user)));
    }

    public void deleteOrder(Article article, User user, String orderApiId) {
        Order order = getByApiId(orderApiId);

        order.validArticle(article);
        order.validUser(user); //판매자와 구매자만
        orderRepository.delete(getByApiId(orderApiId));
    }

    public Order getByApiId(String apiId) {
        return orderRepository.findByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }
}
