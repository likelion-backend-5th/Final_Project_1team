package mutsa.api.service.order;

import jakarta.persistence.EntityManager;
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
class OrderModuleServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderModuleService orderModuleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private EntityManager em;

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
        OrderDetailResponseDto detailOrder = orderModuleService.findDetailOrder(article, user, savedOrder.getApiId());

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
        List<OrderResponseDto> allOrder = orderModuleService.findAllOrder(article, user);

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.size()).isEqualTo(2);

    }

    @Test
    void saveOrder() {
        //given

        //when
        OrderDetailResponseDto orderDetailResponseDto = orderModuleService.saveOrder(article, user);

        //then
        assertThat(orderDetailResponseDto.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void deleteOrder() {
        //given
        Order order = Order.of(article, user);
        Order savedOrder = orderRepository.save(order);

        //when
        orderModuleService.deleteOrder(article, user, savedOrder.getApiId());

        //then
        Optional<Order> byApiId = orderRepository.findByApiId(article.getApiId());
        assertThat(byApiId.isPresent()).isFalse();
    }

    @Test
    void getByApiId() {
        //given
        Order order = Order.of(article, user);
        order = orderRepository.save(order);

        //when
        Order byApiId = orderModuleService.getByApiId(order.getApiId());

        //then
        assertThat(byApiId.getId()).isEqualTo(order.getId());
    }

    @Test
    void getById() {
        //given
        Order order = Order.of(article, user);
        order = orderRepository.save(order);

        //when
        Order byId = orderModuleService.getById(order.getId());

        //then
        assertThat(byId.getId()).isEqualTo(order.getId());
    }
}