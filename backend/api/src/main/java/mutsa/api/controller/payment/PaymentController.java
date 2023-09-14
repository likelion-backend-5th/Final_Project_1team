package mutsa.api.controller.payment;

import lombok.RequiredArgsConstructor;
import mutsa.api.config.common.CommonConfig;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentFailDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import mutsa.api.service.payment.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final CommonConfig commonConfig;

    // 결제 요청을 위한 정보 생성 및 반환
    @GetMapping("/{articleApiId}")
    public ResponseEntity<PaymentDto> getPaymentInfo(@PathVariable String articleApiId) {
        PaymentDto paymentDto = paymentService.getPaymentInfoAndSave(articleApiId);
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }

    // 결제 성공
    @GetMapping("/success")
    public ResponseEntity<PaymentSuccessDto> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount) {
        URI location = paymentService.tossPaymentSuccess(paymentKey, orderId, amount, commonConfig);
        return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
    }

    // 결제 실패
    @GetMapping("/fail")
    public ResponseEntity<PaymentFailDto> tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        paymentService.tossPaymentFail(code, message, orderId);
        return ResponseEntity.ok().body(
                PaymentFailDto.builder()
                        .errorCode(code)
                        .errorMessage(message)
                        .orderId(orderId)
                        .build()
        );
    }
}
