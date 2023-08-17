package mutsa.api.repository;

import jakarta.transaction.Transactional;
import mutsa.api.service.article.ArticleModuleService;
import mutsa.api.service.article.ArticleService;
import mutsa.api.service.user.UserModuleService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.order.OrderRepository;
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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private UserModuleService userService;
    @Mock
    private ArticleModuleService articleService;

    private User user;
    private Article article;

    @BeforeEach
    public void init() {
        User user = User.of("user", "password", "email", "oauthName", null);
        when(userService.getByApiId(any())).thenReturn(user);

        Article article = Article.builder()
                .apiId("test")
                .title("title")
                .description("description")
                .build();
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

        //when
        orderRepository.deleteById(saved.getId());
        Optional<Order> find = orderRepository.findById(saved.getId());

        //then
        Assertions.assertThat(find.isPresent()).isFalse();

    }
}
