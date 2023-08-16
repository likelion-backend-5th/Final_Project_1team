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
    private Long articleId;// UUID로 수정
    private Long userId; //유저 이름이나 UUID로 수정

    public static OrderDetailResponseDto fromEntity(Order order) {
        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto();
        orderDetailResponseDto.apiId = order.getApiId();
        orderDetailResponseDto.orderStatus = order.getOrderStatus();
        orderDetailResponseDto.userId = orderDetailResponseDto.getUserId();
        orderDetailResponseDto.articleId = orderDetailResponseDto.getArticleId();

        return orderDetailResponseDto;
    }
}
