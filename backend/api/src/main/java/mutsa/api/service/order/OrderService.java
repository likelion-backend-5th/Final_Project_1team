package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.CustomPage;
import mutsa.api.dto.order.*;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.filter.order.OrderConsumerFilter;
import mutsa.common.domain.filter.order.OrderSellerFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final UserModuleService userService;
    private final ArticleModuleService articleModuleService;
    private final OrderModuleService orderModuleService;

    public OrderDetailResponseDto findDetailOrder(String articleApiId, String orderApiId, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        return orderModuleService.findDetailOrder(article, user, orderApiId);
    }

    public CustomPage<OrderResponseDto> findAllOrder(String articleApiId, int page, int limit, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        Page<OrderResponseDto> allOrder = orderModuleService.findAllOrder(article, user, page, limit);
        return new CustomPage(allOrder);
    }

    public OrderDetailResponseDto saveOrder(String articleApiId, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        return orderModuleService.saveOrder(article, user);
    }

    public OrderBySellerResponseListDto findByFilterBySeller(OrderSellerFilterDto orderSellerFilterDto, int page, int limit, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = null;
        if (StringUtils.hasText(orderSellerFilterDto.getArticleApiId())) {
            article = articleModuleService.getByApiId(orderSellerFilterDto.getArticleApiId());
        }

        log.info("---------------"+article);
        OrderSellerFilter sellerFilter = OrderSellerFilter.of(OrderStatus.of(orderSellerFilterDto.getOrderStatus()), article);
        String[] sortingProperties = {"id"};

        CustomPage<OrderResponseDto> orderResponseDtoCustomPage = new CustomPage<>(orderModuleService.findByFilterBySeller(user, sellerFilter, sortingProperties, page, limit));
        return OrderBySellerResponseListDto.of(orderResponseDtoCustomPage, orderSellerFilterDto);
    }

    public OrderByConsumerResponseListDto findByFilterByConsumer(OrderConsumerFilterDto orderConsumerFilter, int page, int limit, String currentUsername) {
        User user = userService.getByUsername(currentUsername);

        OrderConsumerFilter consumerFilter = OrderConsumerFilter.of(OrderStatus.of(orderConsumerFilter.getOrderStatus()));
        String[] sortingProperties = {"id"};

        CustomPage<OrderResponseDto> orderResponseDtoCustomPage = new CustomPage<>(orderModuleService.findByFilterByConsumer(user, consumerFilter, sortingProperties, page, limit));
        return OrderByConsumerResponseListDto.of(orderResponseDtoCustomPage, consumerFilter);
    }

    public OrderDetailResponseDto updateOrderStatus(String articleApiId, String orderApiId, OrderStatueRequestDto orderStatueRequestDto, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        return orderModuleService.updateOrderStatus(article, user, orderStatueRequestDto, orderApiId);
    }

    public void deleteOrder(String articleApiId, String orderApiId, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        orderModuleService.deleteOrder(article, user, orderApiId);
    }
}
