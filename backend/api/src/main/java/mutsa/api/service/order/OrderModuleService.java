package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.repository.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<OrderResponseDto> findAllOrder(Article article, User user, int page, int limit) {
        article.validUser(user); //판매자만 확인할 수 있는지

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        return orderRepository.findByArticle(article, pageable).map(OrderResponseDto::fromEntity);
    }

    @Transactional
    public OrderDetailResponseDto saveOrder(Article article, User user) {
        return OrderDetailResponseDto.fromEntity(orderRepository.save(Order.of(article, user)));
    }

    @Transactional
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
