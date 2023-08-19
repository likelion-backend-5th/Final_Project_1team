package mutsa.api.service.order;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.api.dto.order.OrderStatueRequestDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiApplication.class)
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
    private EntityManager entityManager;

    private User seller, consumer, other;
    private Article article;

    @BeforeEach
    public void init() {
        seller = User.of("user1", "password", "email1@", "oauthName1", null, null);
        consumer = User.of("user2", "password", "email2@", "oauthName2", null, null);
        other = User.of("user3", "password", "email3@", "oauthName3", null, null);
        seller = userRepository.save(seller);
        consumer = userRepository.save(consumer);
        other = userRepository.save(other);

        article = Article.builder()
                .title("Pre Article 1")
                .description("Pre Article 1 desc")
                .user(seller)
                .build();

        article = articleRepository.save(article);
    }

    @Test
    void findDetailOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);

        //when
        OrderDetailResponseDto detailOrder = orderModuleService.findDetailOrder(article, seller, savedOrder.getApiId());

        //then
        assertThat(detailOrder.getArticleApiId()).isEqualTo(savedOrder.getArticle().getApiId());
        assertThat(detailOrder.getUsername()).isEqualTo(savedOrder.getUser().getUsername());
    }

    @Test
    @DisplayName("판매자나 소비자가 아닌사람이 확인하려 하는 경우")
    void findDetailOrderOtherPeople() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);

        //when, then
        Assertions.assertThatThrownBy(() -> orderModuleService.findDetailOrder(article, other, savedOrder.getApiId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ORDER_PERMISSION_DENIED.getMessage());
    }


    @Test
    void findAllOrder() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //when
        Page<OrderResponseDto> allOrder = orderModuleService.findAllOrder(article, seller, 0, 20);

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findAllOrderNotSeller() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //when, then
        Assertions.assertThatThrownBy(() -> orderModuleService.findAllOrder(article, consumer, 0, 20))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ARTICLE_PERMISSION_DENIED.getMessage());

        Assertions.assertThatThrownBy(() -> orderModuleService.findAllOrder(article, other, 0, 20))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ARTICLE_PERMISSION_DENIED.getMessage());
    }

    @Test
    void saveOrder() {
        //given

        //when
        OrderDetailResponseDto orderDetailResponseDto = orderModuleService.saveOrder(article, consumer);

        //then
        assertThat(orderDetailResponseDto.getUsername()).isEqualTo(consumer.getUsername());
    }

    @Test
    void updateOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        //when
        orderModuleService.updateOrderStatus(article, consumer, new OrderStatueRequestDto(OrderStatus.END), savedOrder.getApiId());
        entityManager.flush();
        entityManager.clear();

        //then
        Optional<Order> byApiId = orderRepository.findByApiId(order.getApiId());
        Assertions.assertThat(byApiId.get().getOrderStatus()).isEqualTo(OrderStatus.END);
    }


    @Test
    void deleteOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        //when
        orderModuleService.deleteOrder(article, consumer, savedOrder.getApiId());
        entityManager.flush();
        entityManager.clear();

        //then
        Optional<Order> byApiId = orderRepository.findByApiId(order.getApiId());
        assertThat(byApiId.isPresent()).isFalse();

        Optional<Order> byWithDelete = orderRepository.findByWithDelete(order.getId());
        assertThat(byWithDelete.isPresent()).isTrue();
    }

    @Test
    void getByApiId() {
        //given
        Order order = Order.of(article, consumer);
        order = orderRepository.save(order);

        //when
        Order byApiId = orderModuleService.getByApiId(order.getApiId());

        //then
        assertThat(byApiId.getId()).isEqualTo(order.getId());
    }

    @Test
    void getById() {
        //given
        Order order = Order.of(article, consumer);
        order = orderRepository.save(order);

        //when
        Order byId = orderModuleService.getById(order.getId());

        //then
        assertThat(byId.getId()).isEqualTo(order.getId());
    }
}