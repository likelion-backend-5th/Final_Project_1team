package mutsa.common.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;

import java.time.LocalDateTime;
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

    public OrderResponseDto(String orderApiId, String articleApiId, String articleTitle, String consumerName, String sellerName, LocalDateTime date, OrderStatus orderStatus) {
        this.orderApiId = orderApiId;
        this.articleApiId = articleApiId;
        this.articleTitle = articleTitle;
        this.consumerName = consumerName;
        this.sellerName = sellerName;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.orderStatus = orderStatus;
    }

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
