package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderStatusRequestDto;
import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.order.OrderResponseDto;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static mutsa.common.exception.ErrorCode.ORDER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderModuleService {
    private final OrderRepository orderRepository;

    public OrderDetailResponseDto findDetailOrder(Article article, User user, String orderApiId) {
        Order order = getByApiId(orderApiId);

        order.validArticleId(article);
        order.validSellerOrConsumerId(user); //판매자와 구매자만 확인할 수 있도록
        return OrderDetailResponseDto.fromEntity(order);
    }

    public Page<OrderResponseDto> findAllOrder(Article article, User user, String orderStatus, Pageable pageable) {
        validArticleUser(article, user); //판매자만 확인할 수 있는지
        if (orderStatus == null) {
            return orderRepository.findByArticle(article, pageable).map(OrderResponseDto::fromEntity);
        }
        return orderRepository.findByArticleAndOrderStatus(article, pageable, OrderStatus.of(orderStatus)).map(OrderResponseDto::fromEntity);
    }

    @Transactional
    public OrderDetailResponseDto saveOrder(Article article, User user) {
        return OrderDetailResponseDto.fromEntity(orderRepository.save(Order.of(article, user)));
    }

    @Transactional
    public OrderDetailResponseDto updateOrderStatus(Article article, User user, OrderStatusRequestDto orderStatusRequestDto, String orderApiId) {
        Order order = getByApiId(orderApiId);

        order.validArticleId(article);
        order.validSellerOrConsumerId(user); //판매자와 구매자만 상태를 변경할 수 있다.
        order.setOrderStatus(OrderStatus.of(orderStatusRequestDto.getOrderStatus()));
        return OrderDetailResponseDto.fromEntity(order);
    }

    @Transactional
    public void deleteOrder(Article article, User user, String orderApiId) {
        Order order = getByApiId(orderApiId);
        order.validArticleId(article);
        order.validSellerOrConsumerId(user); //판매자와 구매자만
        orderRepository.delete(getByApiId(orderApiId));
    }

    public Page<mutsa.common.dto.order.OrderResponseDto> getOrderByFilter(User user, OrderFilter orderFilter, Pageable pageable) {
        if (orderFilter.getOrderUserType() == OrderFilter.OrderUserType.SELLER) {
            return orderRepository.getOrderByFilterBySeller(orderFilter, user, pageable);
        }

        if (orderFilter.getOrderUserType() == OrderFilter.OrderUserType.CONSUMER) {
            return orderRepository.getOrderByFilterByConsumer(orderFilter, user, pageable);
        }

        return null;
    }


    public Order getByApiId(String apiId) {
        return orderRepository.findByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_FOUND));
    }

    private void validArticleUser(Article article, User user) {
        if (!article.validUser(user)) {
            throw new BusinessException(ErrorCode.ARTICLE_PERMISSION_DENIED);
        }
    }
}
