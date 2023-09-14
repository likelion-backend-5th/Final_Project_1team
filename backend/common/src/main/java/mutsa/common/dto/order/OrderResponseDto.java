package mutsa.common.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static mutsa.common.dto.constants.ImageConstants.DEFAULT_AVATAR_IMAGE;

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
    private String sellerProfileImage;
    private String consumerProfileImage;

    //jpa - query dsl에서 사용
    public OrderResponseDto(String orderApiId, String articleApiId, String articleTitle, String consumerName, String sellerName, LocalDateTime date, OrderStatus orderStatus,String sellerProfileImage, String consumerProfileImage) {
        this.orderApiId = orderApiId;
        this.articleApiId = articleApiId;
        this.articleTitle = articleTitle;
        this.consumerName = consumerName;
        this.sellerName = sellerName;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.orderStatus = orderStatus;
        this.sellerProfileImage = getAvatarImage(sellerProfileImage);
        this.consumerProfileImage = getAvatarImage(consumerProfileImage);
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
        orderResponseDto.sellerProfileImage = getAvatarImage(order.getArticle().getUser().getImageUrl());
        orderResponseDto.consumerProfileImage = getAvatarImage(order.getUser().getImageUrl());
        return orderResponseDto;
    }

    public static String getAvatarImage(String imageUrl) {
        if (StringUtils.hasText(imageUrl)) {
            return imageUrl;
        }
        return DEFAULT_AVATAR_IMAGE;
    }
}
