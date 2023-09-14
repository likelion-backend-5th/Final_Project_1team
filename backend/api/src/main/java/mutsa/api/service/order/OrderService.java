package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.CustomPage;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderFilterDto;
import mutsa.api.dto.order.OrderResponseListDto;
import mutsa.api.dto.order.OrderStatusRequestDto;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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

    public CustomPage<OrderResponseDto> findAllOrder(String articleApiId, String sortOrder, String orderStatus, int page, int limit, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.fromString(sortOrder), "id");
        Page<OrderResponseDto> allOrder = orderModuleService.findAllOrder(article, user, orderStatus, pageable);
        return new CustomPage(allOrder);
    }

    public OrderDetailResponseDto saveOrder(String articleApiId, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        return orderModuleService.saveOrder(article, user);
    }

    public OrderResponseListDto getOrderPage(OrderFilterDto orderFilterDto, String currentUsername) {
        User user = userService.getByUsername(currentUsername);

        Pageable pageable = getPageable(orderFilterDto);
        OrderFilter sellerFilter = OrderFilter.of(orderFilterDto.getUserType(), orderFilterDto.getOrderStatus(), orderFilterDto.getText());

        Page<OrderResponseDto> byFilterBySeller = orderModuleService.getOrderByFilter(user, sellerFilter, pageable);
        CustomPage<OrderResponseDto> orderResponseDtoCustomPage = new CustomPage<>(byFilterBySeller);
        return OrderResponseListDto.of(orderResponseDtoCustomPage, orderFilterDto);
    }

    public OrderDetailResponseDto updateOrderStatus(String articleApiId, String orderApiId, OrderStatusRequestDto orderStatusRequestDto, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        return orderModuleService.updateOrderStatus(article, user, orderStatusRequestDto, orderApiId);
    }

    public void deleteOrder(String articleApiId, String orderApiId, String currentUsername) {
        User user = userService.getByUsername(currentUsername);
        Article article = articleModuleService.getByApiId(articleApiId);

        orderModuleService.deleteOrder(article, user, orderApiId);
    }

    private static Pageable getPageable(OrderFilterDto dto) {
        String[] sortingProperties = {"id"}; // orderSellerFilterDto.getSortingProperties();로 추후에 사용가능
        Sort.Direction direction = Sort.Direction.fromString(dto.getSortOrder());
        return PageRequest.of(dto.getPage(), dto.getLimit(), direction, sortingProperties);
    }
}
