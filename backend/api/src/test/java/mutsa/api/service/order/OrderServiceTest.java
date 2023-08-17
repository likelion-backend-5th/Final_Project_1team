package mutsa.api.service.order;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class OrderServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User user;
    private Article article;

    @BeforeEach
    public void init() {
        user = User.of("user", "password", "email", "oauthName", null, null);
        user = userRepository.save(user);

        article = Article.builder()
                .title("Pre Article 1")
                .description("Pre Article 1 desc")
                .user(user)
                .build();

        article = articleRepository.save(article);
    }

    @Test
    void findDetailOrder() {
        //given
        Order order = Order.of(article, user);
        Order savedOrder = orderRepository.save(order);

        //when
        OrderDetailResponseDto detailOrder = orderService.findDetailOrder(article.getApiId(), savedOrder.getApiId(), user.getUsername());

        //then
        assertThat(detailOrder.getArticleApiId()).isEqualTo(savedOrder.getArticle().getApiId());
        assertThat(detailOrder.getUsername()).isEqualTo(savedOrder.getUser().getUsername());
    }

    @Test
    void findAllOrder() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, user));
        Order savedOrder2 = orderRepository.save(Order.of(article, user));

        //when
        List<OrderResponseDto> allOrder = orderService.findAllOrder(article.getApiId(), user.getUsername());

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.size()).isEqualTo(2);

    }

    @Test
    void saveOrder() {
        //given

        //when
        OrderDetailResponseDto orderDetailResponseDto = orderService.saveOrder(article.getApiId(), user.getUsername());

        //then
        assertThat(orderDetailResponseDto.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void deleteOrder() {
        //given
        Order order = Order.of(article, user);
        Order savedOrder = orderRepository.save(order);

        //when
        orderService.deleteOrder(article.getApiId(), savedOrder.getApiId(), user.getUsername());

        //then
        Optional<Order> byApiId = orderRepository.findByApiId(article.getApiId());
        assertThat(byApiId.isPresent()).isFalse();
    }
}