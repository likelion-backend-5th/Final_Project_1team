package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.CustomPage;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.api.dto.order.OrderStatueRequestDto;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
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
