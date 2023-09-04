package mutsa.api.service.payment;

import lombok.RequiredArgsConstructor;
import mutsa.api.config.payment.TossPaymentConfig;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.payment.Payment;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.payment.PaymentRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentModuleService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentConfig tossPaymentConfig;
    private final ArticleRepository articleRepository;
    private final OrderRepository orderRepository;

    // 결제 요청을 위한 정보 생성 및 반환
    @Transactional
    public PaymentDto getPaymentInfoAndSave(String articleApiId) {
        User user = getCurrentUser();
        Article article = getArticleByApiId(articleApiId);
        Order savedOrder = saveOrder(article, user);
        PaymentDto paymentDto = createPaymentDto(user, article, savedOrder);
        savePayment(paymentDto, savedOrder);
        return paymentDto;
    }

    // 결제 요청 성공 로직
    @Transactional
    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
        updatePaymentAfterSuccess(payment, paymentKey);
        return result;
    }

    // 실제 결제 서버에 요청 전달 및 채택
    @Transactional
    public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("amount", amount);
        PaymentSuccessDto result = restTemplate.postForObject(TossPaymentConfig.URL + paymentKey, new HttpEntity<>(params, headers), PaymentSuccessDto.class);
        return result;
    }

    // 결제 실패 로직
    @Transactional
    public void tossPaymentFail(String code, String message, String orderId) {
        Payment payment = paymentRepository.findByOrderKey(orderId).orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
        payment.updateFail(message, orderId);
    }

    // 현재 유저 반환
    private User getCurrentUser() {
        return userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 게시글 조회
    private Article getArticleByApiId(String articleApiId) {
        return articleRepository.findByApiId(articleApiId).orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    // 주문 생성 및 저장
    private Order saveOrder(Article article, User user) {
        return orderRepository.save(Order.of(article, user));
    }

    // 결제 정보 DTO 생성
    private PaymentDto createPaymentDto(User user, Article article, Order savedOrder) {
        return PaymentDto.builder().customerApiId(user.getApiId()).amount(119000L).orderId(savedOrder.getApiId()).orderName(article.getTitle()).customerEmail(user.getEmail()).customerName(user.getUsername()).build();
    }

    // 결제 정보 저장
    private void savePayment(PaymentDto paymentDto, Order savedOrder) {
        Payment savedPayment = paymentDto.toEntity();
        savedPayment.setOrder(savedOrder);
        paymentRepository.save(savedPayment);
    }

    // 결제 유효성 검사
    public Payment verifyPayment(String orderId, Long amount) {
        Payment payment = paymentRepository.findByOrderKey(orderId).orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
        if (!payment.getAmount().equals(amount)) {
            throw new BusinessException(ErrorCode.PAYMENT_INVALID_AMOUNT);
        }
        return payment;
    }

    // 헤더 설정 및 반환
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(Base64.getEncoder().encode((tossPaymentConfig.getTestSecretKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    // 결제 성공 시 정보 수정
    private void updatePaymentAfterSuccess(Payment payment, String paymentKey) {
        payment.updateAfterSuccess(paymentKey);
    }
}
