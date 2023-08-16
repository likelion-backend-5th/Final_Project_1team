package mutsa.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private String apiId;
    private OrderStatus orderStatus;
    private Long articleId;// UUID로 수정
    private Long userId; //유저 이름이나 UUID로 수정

    public static OrderResponseDto fromEntity(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.apiId = order.getApiId();
        orderResponseDto.orderStatus = order.getOrderStatus();
        orderResponseDto.userId = orderResponseDto.getUserId();
        orderResponseDto.articleId = orderResponseDto.getArticleId();

        return orderResponseDto;
    }
}
