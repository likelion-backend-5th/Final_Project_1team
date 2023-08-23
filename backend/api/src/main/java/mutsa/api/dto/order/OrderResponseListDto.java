package mutsa.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.api.dto.CustomPage;

@Getter
@NoArgsConstructor
public class OrderResponseListDto {
    private CustomPage<OrderResponseDto> orderResponseDtos;
    private OrderFilterDto orderFilterDto;

    public static OrderResponseListDto of(CustomPage<OrderResponseDto> orderResponseDtoCustomPage, OrderFilterDto orderFilterDto) {
        OrderResponseListDto orderResponseListDto = new OrderResponseListDto();
        orderResponseListDto.orderResponseDtos = orderResponseDtoCustomPage;
        orderResponseListDto.orderFilterDto = orderFilterDto;
        return orderResponseListDto;
    }
}
