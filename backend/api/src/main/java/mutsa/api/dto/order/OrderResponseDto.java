package mutsa.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private String orderApiId;
    private String articleApiId;
    private String articleTitle;
    private String consumerName;
    private String sellerName;
    private String date;
    private OrderStatus orderStatus;

    public static OrderResponseDto fromEntity(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.orderApiId = order.getApiId();
        orderResponseDto.articleApiId = order.getArticle().getApiId();
        orderResponseDto.articleTitle = order.getArticle().getTitle();
        orderResponseDto.consumerName = order.getUser().getUsername();
        orderResponseDto.sellerName = order.getArticle().getUser().getUsername();
        orderResponseDto.date = order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        orderResponseDto.orderStatus = order.getOrderStatus();

        return orderResponseDto;
    }
}
