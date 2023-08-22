package mutsa.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.api.dto.CustomPage;
import mutsa.common.domain.filter.order.OrderConsumerFilter;

@Getter
@NoArgsConstructor
public class OrderByConsumerResponseListDto {
    private CustomPage<OrderResponseDto> orderResponseDtos;
    private OrderConsumerFilter orderConsumerFilter;

    public static OrderByConsumerResponseListDto of(CustomPage<OrderResponseDto> orderResponseDtoCustomPage, OrderConsumerFilter orderConsumerFilter) {
        OrderByConsumerResponseListDto orderBySellerResponseListDto = new OrderByConsumerResponseListDto();
        orderBySellerResponseListDto.orderResponseDtos = orderResponseDtoCustomPage;
        orderBySellerResponseListDto.orderConsumerFilter = orderConsumerFilter;
        return orderBySellerResponseListDto;
    }
}
