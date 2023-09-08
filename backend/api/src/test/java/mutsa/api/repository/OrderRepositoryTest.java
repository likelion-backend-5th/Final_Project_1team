package mutsa.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.ApiApplication;
import mutsa.api.config.TestRedisConfiguration;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.order.OrderRepository;
import mutsa.common.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ApiApplication.class, TestRedisConfiguration.class})
@ActiveProfiles("test")
@Transactional
@Slf4j
public class OrderRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private UserModuleService userService;
    @Mock
    private ArticleModuleService articleService;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Article article;


    @BeforeEach
    public void init() {
        user = userRepository.save(User.of("user", "password", "email", "oauthName", null, null));
        when(userService.getByApiId(any())).thenReturn(user);

        article = articleRepository.save(Article.builder()
                .apiId("test")
                .title("title")
                .description("description")
                .build());

        when(articleService.getByApiId(any())).thenReturn(article);
    }

    @Test
    @DisplayName("주문 생성")
    public void createOrder() {
        //given
        Order order = Order.of(article, user);

        //when
        Order saved = orderRepository.save(order);

        //then
        Assertions.assertThat(saved.getArticle()).isEqualTo(article);
        Assertions.assertThat(saved.getUser()).isEqualTo(user);
        Assertions.assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.PROGRESS);
    }

    @Test
    @DisplayName("주문 삭제")
    public void deleteOrder() {
        //given
        Order saved = orderRepository.save(Order.of(article, user));
        entityManager.flush();
        entityManager.clear();

        //when
        orderRepository.deleteById(saved.getId());
        entityManager.flush();
        entityManager.clear();

        //then
        Optional<Order> find = orderRepository.findById(saved.getId());
        Assertions.assertThat(find.isPresent()).isFalse();


        Optional<Order> byWithDelete = orderRepository.findByWithDelete(saved.getId());
        log.info("삭제 상태 확인 : {}", byWithDelete.get().getStatus());
        Assertions.assertThat(byWithDelete.get().getStatus()).isEqualTo(Status.DELETED);
    }
}
