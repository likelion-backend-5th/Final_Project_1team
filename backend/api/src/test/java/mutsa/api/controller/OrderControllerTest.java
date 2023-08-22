package mutsa.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.order.OrderStatueRequestDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@ActiveProfiles("test")
@Slf4j
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EntityManager entityManager;

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    private User seller, consumer;
    private Article article;

    @BeforeAll
    public static void beforeAll() {
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    public void init() {
        seller = User.of("user1", "password", "email1@", "oauthName1", null, null);
        consumer = User.of("user2", "password", "email2@", "oauthName2", null, null);
        seller = userRepository.save(seller);
        consumer = userRepository.save(consumer);

        article = Article.builder()
                .title("Pre Article 1")
                .description("Pre Article 1 desc")
                .user(seller)
                .build();

        article = articleRepository.save(article);
    }


    @DisplayName("주문 생성")
    @Test
    void saveOrder() throws Exception {
        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());

        //when, then
        mockMvc.perform(post("/api/articles/{articleApiId}/order", article.getApiId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/order/주문 생성",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("orderStatus").value(OrderStatus.PROGRESS.name()),
                        jsonPath("username").value(consumer.getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        assertThat(orderRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 단일건 조회")
    void getDetailOrder() throws Exception {
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());

        //when, then
        mockMvc.perform(get("/api/articles/{articleApiId}/order/{orderApiId}", article.getApiId(), savedOrder1.getApiId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/order/주문 단일건 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("apiId").value(savedOrder1.getApiId()),
                        jsonPath("username").value(consumer.getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @DisplayName("주문 게시글 별 조회(판매자)")
    @Test
    void getAllOrder() throws Exception {
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder3 = orderRepository.save(Order.of(article, consumer));

        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(seller.getUsername());

        //when
        ResultActions perform = mockMvc.perform(get("/api/articles/{articleApiId}/order", article.getApiId())
                        .param("page", String.valueOf(0))
                        .param("limit", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/order/주문 게시글별 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("pageable.totalElements", equalTo(3)),
                jsonPath("pageable.totalPages", equalTo(1)),
                jsonPath("pageable.numberOfElements", equalTo(3))
        );

    }

    @DisplayName("주문 상태 수정")
    @Test
    void updateOrder() throws Exception {
        Order savedOrder = orderRepository.save(Order.of(article, consumer));
        entityManager.flush();
        entityManager.clear();

        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());
        OrderStatueRequestDto dto = new OrderStatueRequestDto("END");
        String requestBody = new ObjectMapper().writeValueAsString(dto);


        //when
        mockMvc.perform(put("/api/articles/{articleApiId}/order/{orderApiId}", article.getApiId(), savedOrder.getApiId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/order/주문 싱태 수정",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));


        //then
        entityManager.flush();
        entityManager.clear();
        assertThat(orderRepository.findById(savedOrder.getId()).isEmpty()).isFalse();
        assertThat(orderRepository.findById(savedOrder.getId()).get().getOrderStatus()).isEqualTo(OrderStatus.END);
    }


    @DisplayName("주문 삭제")
    @Test
    void deleteOrder() throws Exception {
        Order savedOrder = orderRepository.save(Order.of(article, consumer));
        entityManager.flush();
        entityManager.clear();
        //given
        when(SecurityUtil.getCurrentUsername()).thenReturn(consumer.getUsername());

        //when
        mockMvc.perform(delete("/api/articles/{articleApiId}/order/{orderApiId}", article.getApiId(), savedOrder.getApiId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/order/주문 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));


        //then
        entityManager.flush();
        entityManager.clear();
        assertThat(orderRepository.findById(savedOrder.getId())).isEmpty();
        assertThat(orderRepository.findByWithDelete(savedOrder.getId()).isEmpty()).isFalse();
    }
}