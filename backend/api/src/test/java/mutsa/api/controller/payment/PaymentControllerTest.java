package mutsa.api.controller.payment;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import mutsa.api.service.payment.PaymentModuleService;
import mutsa.api.service.payment.PaymentService;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentModuleService paymentModuleService;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    private User consumer;
    private Article article;
    private Payment payment;
    private Order order;

    @BeforeAll
    public static void beforeAll() {
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    public void setup() {
        initializeEntities();
        setupMocks();
    }

    private void initializeEntities() {
        consumer = User.of("user2", "password", "email2@", "oauthName2", null, null);
        consumer = userRepository.save(consumer);

        article = Article.builder()
                .title("Pre Article 1")
                .description("Pre Article 1 desc")
                .user(consumer)
                .price(129000L)
                .build();
        article = articleRepository.save(article);

        order = orderRepository.save(Order.of(article, consumer));
        payment = Payment.of(PayType.CARD, article, order);
        payment = paymentRepository.save(payment);
    }

    private void setupMocks() {
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());

        PaymentSuccessDto mockSuccessDto = new PaymentSuccessDto();
        when(paymentModuleService.requestPaymentAccept(anyString(), anyString(), anyLong()))
                .thenReturn(mockSuccessDto);

        PaymentDto paymentDto = PaymentDto.builder()
                .customerApiId(consumer.getApiId())
                .amount(article.getPrice())
                .orderId(order.getApiId())
                .orderName(article.getTitle())
                .customerEmail(consumer.getEmail())
                .customerName(consumer.getUsername())
                .build();
        when(paymentService.getPaymentInfoAndSave(anyString())).thenReturn(paymentDto);
    }

    @DisplayName("결제 정보 확인")
    @Test
    void getPaymentInfo() throws Exception {
        mockMvc.perform(get("/api/v1/payments/" + article.getApiId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.customerApiId").value(consumer.getApiId()),
                        jsonPath("$.amount").value(129000L),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        Assertions.assertThat(paymentRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("결제 성공")
    @Test
    void tossPaymentSuccess() throws Exception {
        performPaymentApiCall("/api/v1/payments/success", "mockPaymentKey")
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("결제 실패")
    @Test
    void tossPaymentFail() throws Exception {
        mockMvc.perform(get("/api/v1/payments/fail")
                        .param("code", "mockErrorCode")
                        .param("message", "mockErrorMessage")
                        .param("orderId", order.getApiId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value("mockErrorCode"))
                .andExpect(jsonPath("$.errorMessage").value("mockErrorMessage"))
                .andExpect(jsonPath("$.orderId").value(order.getApiId()));
    }

    private ResultActions performPaymentApiCall(String url, String paymentKey) throws Exception {
        return mockMvc.perform(get(url)
                        .param("paymentKey", paymentKey)
                        .param("orderId", order.getApiId())
                        .param("amount", String.valueOf(article.getPrice()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}
