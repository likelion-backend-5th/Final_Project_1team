package mutsa.api.service.payment;

import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.config.common.CommonConfig;
import mutsa.api.dto.payment.PaymentDto;
import mutsa.api.dto.payment.PaymentSuccessDto;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@Slf4j
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @MockBean
    PaymentModuleService paymentModuleService;

    PaymentDto paymentDtoMock;
    PaymentSuccessDto paymentSuccessDtoMock;
    URI mockUri;

    @BeforeEach
    void setUp() {
        paymentDtoMock = new PaymentDto();
        paymentSuccessDtoMock = new PaymentSuccessDto();
        mockUri = URI.create("http://example.com");

        when(paymentModuleService.getPaymentInfoAndSave(anyString())).thenReturn(paymentDtoMock);
        when(paymentModuleService.tossPaymentSuccess(anyString(), anyString(), anyLong())).thenReturn(paymentSuccessDtoMock);
        when(paymentModuleService.getSuccessRedirectLocation(anyString(), any())).thenReturn(mockUri);
    }

    @DisplayName("결제 정보 확인 및 저장")
    @Test
    void testGetPaymentInfoAndSave() {
        PaymentDto result = paymentService.getPaymentInfoAndSave("articleApiIdMock");
        assertEquals(paymentDtoMock, result);
    }

    @DisplayName("결제 성공 DTO")
    @Test
    void testTossPaymentSuccessDto() {
        PaymentSuccessDto result = paymentService.tossPaymentSuccess("paymentKeyMock", "orderIdMock", 1000L);
        assertEquals(paymentSuccessDtoMock, result);
    }

    @DisplayName("결제 성공 Uri")
    @Test
    void testTossPaymentSuccessUri() {
        URI result = paymentService.tossPaymentSuccess("paymentKeyMock", "orderIdMock", 1000L, new CommonConfig());
        assertEquals(mockUri, result);
    }

    @DisplayName("결제 실패")
    @Test
    void testTossPaymentFail() {
        doNothing().when(paymentModuleService).tossPaymentFail(anyString(), anyString(), anyString());
        paymentService.tossPaymentFail("codeMock", "messageMock", "orderIdMock");
        verify(paymentModuleService, times(1)).tossPaymentFail("codeMock", "messageMock", "orderIdMock");
    }
}
