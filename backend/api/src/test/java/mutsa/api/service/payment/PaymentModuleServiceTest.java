package mutsa.api.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.config.payment.TossPaymentConfig;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.payment.PayType;
import mutsa.common.domain.models.payment.Payment;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.payment.PaymentRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@Slf4j
class PaymentModuleServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentModuleService paymentModuleService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    private User buyer, seller;
    private Article article;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        setupSecurityUtilMock();
        initTestData();
    }

    private void setupSecurityUtilMock() {
        if (securityUtilMockedStatic == null) {
            securityUtilMockedStatic = mockStatic(SecurityUtil.class);
        }
        securityUtilMockedStatic.when(SecurityUtil::getCurrentUsername).thenReturn("buyer1");
    }

    private void initTestData() {
        buyer = createAndSaveUser("buyer1", "buyerEmail@", "buyerOauthName");
        seller = createAndSaveUser("seller1", "sellerEmail@", "sellerOauthName");
        article = createAndSaveArticle("Sample Article", "Sample Article desc", 50000L, seller);
    }

    private User createAndSaveUser(String username, String email, String oauthName) {
        User user = User.of(username, "password", email, oauthName, null, null);
        return userRepository.save(user);
    }

    private Payment createAndSavePayment(Article article, Order order) {
        Payment payment = Payment.of(PayType.CARD, article, order);
        return paymentRepository.save(payment);
    }

    private Article createAndSaveArticle(String title, String description, Long price, User user) {
        Article newArticle = Article.builder()
                .title(title)
                .description(description)
                .user(user)
                .price(price)
                .build();
        return articleRepository.save(newArticle);
    }

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

    @DisplayName("결제 정보 확인 및 저장")
    @Test
    void getPaymentInfoAndSaveTest() {
        PaymentDto paymentDto = paymentModuleService.getPaymentInfoAndSave(article.getApiId());
        assertThat(paymentDto.getCustomerApiId()).isEqualTo(buyer.getApiId());
        assertThat(paymentDto.getAmount()).isEqualTo(article.getPrice());
    }

    @DisplayName("결제 성공")
    @Test
    void tossPaymentSuccessTest() {
        // Given
        Order savedOrder = createAndSaveOrder();
        log.info(savedOrder.getApiId());
        mockExternalPaymentApi(savedOrder.getApiId(), 50000L);
        createAndSavePayment(article, savedOrder);

        // When
        PaymentSuccessDto result = paymentModuleService.tossPaymentSuccess("somePaymentKey", savedOrder.getApiId(), 50000L);

        // Then
        assertThat(result).isNotNull();
        mockServer.verify();
    }

    private Order createAndSaveOrder() {
        Order order = Order.of(article, buyer);
        return orderRepository.save(order);
    }

    private void mockExternalPaymentApi(String orderId, Long amount) {
        PaymentSuccessDto expectedResponse = new PaymentSuccessDto();
        try {
            mockServer.expect(ExpectedCount.once(),
                            requestTo(TossPaymentConfig.URL + "somePaymentKey"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withSuccess(
                            new ObjectMapper().writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mocking external payment API", e);
        }
    }
}
