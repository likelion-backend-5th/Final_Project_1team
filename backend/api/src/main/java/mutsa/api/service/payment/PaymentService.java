package mutsa.api.service.payment;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentModuleService paymentModuleService;

    // 결제 요청을 위한 정보 생성 및 반환
    public PaymentDto getPaymentInfoAndSave(String articleApiId) {
        return paymentModuleService.getPaymentInfoAndSave(articleApiId);
    }

    // 결제 요청 성공 로직
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        return paymentModuleService.tossPaymentSuccess(paymentKey, orderId, amount);
    }

    // 결제 실패 로직
    public void tossPaymentFail(String code, String message, String orderId) {
        paymentModuleService.tossPaymentFail(code, message, orderId);
    }
}
