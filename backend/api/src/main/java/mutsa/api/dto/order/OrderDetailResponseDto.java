package mutsa.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDto {
    private String apiId;
    private OrderStatus orderStatus;
    private String articleId;
    private String username;

    public static OrderDetailResponseDto fromEntity(Order order) {
        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto();
        orderDetailResponseDto.apiId = order.getApiId();
        orderDetailResponseDto.orderStatus = order.getOrderStatus();
        orderDetailResponseDto.articleId = orderDetailResponseDto.getApiId();
        orderDetailResponseDto.username = order.getUser().getUsername();

        return orderDetailResponseDto;
    }
}
