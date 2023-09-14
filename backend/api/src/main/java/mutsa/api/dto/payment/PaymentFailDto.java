package mutsa.api.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentFailDto {
    String errorCode;
    String errorMessage;
    String orderId;
}
