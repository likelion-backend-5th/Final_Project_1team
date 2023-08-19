package mutsa.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mutsa.common.domain.models.order.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatueRequestDto {
    OrderStatus orderStatus;
}
