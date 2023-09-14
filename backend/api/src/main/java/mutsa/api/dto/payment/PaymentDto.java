package mutsa.api.dto.payment;

import lombok.*;
import mutsa.common.domain.models.payment.Payment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String customerApiId;       // 구매자 ApiId
    private Long amount;                // 결제 금액
    private String orderId;             // 결제 요청 key
    private String orderName;           // 주문 이름
    private String customerEmail;       // 주문자 이메일
    private String customerName;        // 주문자 username

    public Payment toEntity() {
        return Payment.builder()
                .amount(amount)
                .orderName(orderName)
                .orderKey(orderId)
                .build();
    }
}
