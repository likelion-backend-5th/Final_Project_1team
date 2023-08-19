package mutsa.api.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.dto.review.ReviewRequestDto;
import mutsa.api.dto.review.ReviewUpdateDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.review.Review;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.review.ReviewRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ApiApplication.class)
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private EntityManager entityManager;

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    private User reviewer1, reviewer2;
    private Article article;
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
    public void init() {

        reviewer1 = User.of("user1", "password", "email1@", "oauthName1", null, null);
        reviewer1 = userRepository.save(reviewer1);

        reviewer2 = User.of("user2", "password", "email2@", "oauthName2", null, null);
        reviewer2 = userRepository.save(reviewer2);

        User seller = User.of("seller", "password", "sellerEmail@", "sellerOauthName", null, null);
        seller = userRepository.save(seller);

        article = Article.builder()
            .title("Pre Article 1")
            .description("Pre Article 1 desc")
            .user(seller)
            .build();
        article = articleRepository.save(article);

        order = Order.of(article, reviewer1);
        order.setOrderStatus(OrderStatus.END);
        order = orderRepository.save(order);
    }

    @DisplayName("후기 생성")
    @Test
    void saveReview() throws Exception {
        // given
        when(SecurityUtil.getCurrentUsername()).thenReturn(reviewer1.getUsername());
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();

        reviewRequestDto.setContent("Review Test");
        reviewRequestDto.setPoint(5);

        // when
        // then
        mockMvc.perform(post("/api/article/{articleApiId}/order/{orderApiId}/review", article.getApiId(), order.getApiId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(reviewRequestDto)))
            .andExpectAll(
                status().is2xxSuccessful(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("username").value(reviewer1.getUsername())
            );

        assertThat(reviewRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("후기 단일 조회")
    @Test
    void getReview() throws Exception {
        // given
        Review review = reviewRepository.save(Review.of(reviewer1, article, "content1", 1));

        // when
        // then
        mockMvc.perform(get("/api/review/{reviewApiId}", review.getApiId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().is2xxSuccessful(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("apiId").value(review.getApiId()),
                jsonPath("username").value(reviewer1.getUsername())
            );
    }

    @DisplayName("후기 수정")
    @Test
    void updateReview() throws Exception {
        // given
        when(SecurityUtil.getCurrentUsername()).thenReturn(reviewer1.getUsername());
        Review review = reviewRepository.save(Review.of(reviewer1, article, "content1", 1));
        ReviewUpdateDto updateDto = new ReviewUpdateDto();
        updateDto.setContent("test Content");
        updateDto.setPoint(3);

        // when
        // then
        mockMvc.perform(put("/api/review/{reviewApiId}", review.getApiId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updateDto)))
            .andExpectAll(
                status().is2xxSuccessful(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("content").value("test Content"),
                jsonPath("point").value(3)
            );
    }

    @DisplayName("후기 삭제")
    @Test
    void deleteReview() throws Exception {
        // given
        when(SecurityUtil.getCurrentUsername()).thenReturn(reviewer1.getUsername());
        Review review = reviewRepository.save(Review.of(reviewer1, article, "content1", 1));
        entityManager.clear();

        // when
        // then
        // TODO Soft Delete 로 수정
        mockMvc.perform(delete("/api/review/{reviewApiId}", review.getApiId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().is2xxSuccessful(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("message").value("후기를 삭제했습니다.")
            );
    }

    private static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
