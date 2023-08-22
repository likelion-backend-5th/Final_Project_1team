package mutsa.api.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.api.dto.CustomPage;

@Getter
@NoArgsConstructor
public class OrderBySellerResponseListDto {
    private CustomPage<OrderResponseDto> orderResponseDtos;
    private OrderSellerFilterDto orderSellerFilterDto;

    public static OrderBySellerResponseListDto of(CustomPage<OrderResponseDto> orderResponseDtoCustomPage, OrderSellerFilterDto orderSellerFilterDto) {
        OrderBySellerResponseListDto orderBySellerResponseListDto = new OrderBySellerResponseListDto();
        orderBySellerResponseListDto.orderResponseDtos = orderResponseDtoCustomPage;
        orderBySellerResponseListDto.orderSellerFilterDto = orderSellerFilterDto;
        return orderBySellerResponseListDto;
    }
}
