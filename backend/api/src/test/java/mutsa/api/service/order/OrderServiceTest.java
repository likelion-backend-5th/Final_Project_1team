package mutsa.api.service.order;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.CustomPage;
import mutsa.api.dto.order.*;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    @Autowired
    private EntityManager entityManager;

    private User seller, consumer;
    private Article article;

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

    @Test
    void findDetailOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);

        //when
        OrderDetailResponseDto detailOrder = orderService.findDetailOrder(article.getApiId(), savedOrder.getApiId(), seller.getUsername());

        //then
        assertThat(detailOrder.getArticleApiId()).isEqualTo(savedOrder.getArticle().getApiId());
        assertThat(detailOrder.getUsername()).isEqualTo(savedOrder.getUser().getUsername());
    }

    @Test
    void findAllOrder() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //when
        CustomPage<OrderResponseDto> allOrder = orderService.findAllOrder(article.getApiId(), 0, 20, seller.getUsername());

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.getPageable().getTotalElements()).isEqualTo(2);

    }

    @Test
    void saveOrder() {
        //given

        //when
        OrderDetailResponseDto orderDetailResponseDto = orderService.saveOrder(article.getApiId(), seller.getUsername());

        //then
        assertThat(orderDetailResponseDto.getUsername()).isEqualTo(seller.getUsername());
    }

    @Test
    void findByFilterBySeller() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //when
        CustomPage<OrderResponseDto> allOrder = orderService.findByFilterBySeller(new OrderSellerFilterDto(article.getApiId(), OrderStatus.PROGRESS.name()), 0, 20, seller.getUsername()).getOrderResponseDtos();

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.getPageable().getTotalElements()).isEqualTo(2);
    }

    @Test
    void findByFilterBySeller2() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        //when
        CustomPage<OrderResponseDto> orderResponseDtos = orderService.findByFilterBySeller(new OrderSellerFilterDto("", OrderStatus.PROGRESS.name()), 0, 20, seller.getUsername()).getOrderResponseDtos();

        //then
        assertThat(orderResponseDtos.getPageable().getTotalElements()).isEqualTo(2);
    }


    @Test
    void findByFilterByConsumer() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, seller));

        //when
        OrderByConsumerResponseListDto byFilterByConsumer = orderService.findByFilterByConsumer(new OrderConsumerFilterDto(OrderStatus.PROGRESS.name()), 0, 20, consumer.getUsername());

        //then
        assertThat(byFilterByConsumer.getOrderResponseDtos().getPageable().getTotalPages()).isEqualTo(1);
    }

    @Test
    void updateOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        //when
        orderService.updateOrderStatus(article.getApiId(), savedOrder.getApiId(), new OrderStatueRequestDto("END"), consumer.getUsername());
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

        //when
        orderService.deleteOrder(article.getApiId(), savedOrder.getApiId(), seller.getUsername());
        entityManager.flush();
        entityManager.clear();

        //then
        Optional<Order> byApiId = orderRepository.findByApiId(article.getApiId());
        assertThat(byApiId.isPresent()).isFalse();
    }
}