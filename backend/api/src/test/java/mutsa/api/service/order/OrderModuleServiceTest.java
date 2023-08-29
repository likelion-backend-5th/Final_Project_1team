package mutsa.api.service.order;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderStatusRequestDto;
import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.dto.order.OrderResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
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
    private Article article, article2;

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

        article2 = Article.builder()
                .title("Pre Article 2")
                .description("Pre Article 2 desc")
                .user(seller)
                .build();

        article2 = articleRepository.save(article2);
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
        assertThat(detailOrder.getConsumerName()).isEqualTo(savedOrder.getUser().getUsername());
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
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<OrderResponseDto> allOrder = orderModuleService.findAllOrder(article, seller, null, pageable);

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
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Assertions.assertThatThrownBy(() -> orderModuleService.findAllOrder(article, consumer, null, pageable))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ARTICLE_PERMISSION_DENIED.getMessage());

        Assertions.assertThatThrownBy(() -> orderModuleService.findAllOrder(article, other, null, pageable))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.ARTICLE_PERMISSION_DENIED.getMessage());
    }

    @Test
    void findByFilterBySeller() {
        //given
        Order savedOrder1 = orderRepository.save(Order.of(article, consumer));
        Order savedOrder2 = orderRepository.save(Order.of(article, consumer));

        String[] sortingProperties = {"id"};
        Sort.Direction direction = Sort.Direction.fromString("asc");
        PageRequest pageable = PageRequest.of(0, 10, direction, sortingProperties);
        OrderFilter orderFilter = OrderFilter.of("SELLER", OrderStatus.PROGRESS.name(), "");

        //when
        Page<OrderResponseDto> allOrder = orderModuleService.getOrderByFilter(seller, orderFilter, pageable);

        //then
        log.info(allOrder.toString());
        assertThat(allOrder.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findByFilterBySeller2() {
        //given
        List<Order> dummy = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dummy.add(Order.of(article, consumer));
            dummy.add(Order.of(article2, consumer));
        }
        orderRepository.saveAll(dummy);


        String[] sortingProperties = {"id"};
        Sort.Direction direction = Sort.Direction.fromString("asc");
        PageRequest pageable = PageRequest.of(0, 10, direction, sortingProperties);
        OrderFilter orderFilter = OrderFilter.of("SELLER", OrderStatus.PROGRESS.name(), "Article");

        //when
        Page<OrderResponseDto> allOrder = orderModuleService.getOrderByFilter(seller, orderFilter, pageable);

        //then
        log.info(allOrder.getContent().toString());
        assertThat(allOrder.getTotalElements()).isEqualTo(20);
    }

    @Test
    void findByFilterByConsumer() {
        //given
        List<Order> dummy = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dummy.add(Order.of(article, consumer));
        }
        dummy.add(Order.of(article, seller));
        orderRepository.saveAll(dummy);

        String[] sortingProperties = {"id"};
        Sort.Direction direction = Sort.Direction.fromString("asc");
        PageRequest pageable = PageRequest.of(0, 10, direction, sortingProperties);
        OrderFilter orderFilter = OrderFilter.of("CONSUMER", OrderStatus.PROGRESS.name(), "");

        //when
        Page<OrderResponseDto> allOrder = orderModuleService.getOrderByFilter(consumer, orderFilter, pageable);

        //then
        assertThat(allOrder.getTotalElements()).isEqualTo(20);
    }

    @Test
    void saveOrder() {
        //given

        //when
        OrderDetailResponseDto orderDetailResponseDto = orderModuleService.saveOrder(article, consumer);

        //then
        assertThat(orderDetailResponseDto.getConsumerName()).isEqualTo(consumer.getUsername());
    }

    @Test
    void updateOrder() {
        //given
        Order order = Order.of(article, consumer);
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        //when
        orderModuleService.updateOrderStatus(article, consumer, new OrderStatusRequestDto("END"), savedOrder.getApiId());
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