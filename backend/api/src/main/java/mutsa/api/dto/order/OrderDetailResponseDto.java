package mutsa.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.api.util.DateTimeFormatterUtil;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;

@Getter
@NoArgsConstructor
public class OrderDetailResponseDto {
    private String orderApiId;
    private String articleApiId;
    private String articleTitle;
    private String articleDescription;
    private String articleThumbnail;
    private String consumerName;
    private String sellerName;
    private String date;
    private OrderStatus orderStatus;
    private Long amount;

    public static OrderDetailResponseDto fromEntity(Order order) {
        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto();
        orderDetailResponseDto.orderApiId = order.getApiId();
        orderDetailResponseDto.articleApiId = order.getArticle().getApiId();
        orderDetailResponseDto.articleTitle = order.getArticle().getTitle();
        orderDetailResponseDto.articleDescription = order.getArticle().getDescription();
        orderDetailResponseDto.articleThumbnail = order.getArticle().getThumbnail();
        orderDetailResponseDto.consumerName = order.getUser().getUsername();
        orderDetailResponseDto.sellerName = order.getArticle().getUser().getUsername();
        orderDetailResponseDto.date = DateTimeFormatterUtil.formatLocalDateTime(order.getCreatedAt());
        orderDetailResponseDto.orderStatus = order.getOrderStatus();
        orderDetailResponseDto.amount = (order.getPayment() == null) ? null : order.getPayment().getAmount();

        return orderDetailResponseDto;
    }
}
