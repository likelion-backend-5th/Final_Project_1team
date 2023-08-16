package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.api.service.article.ArticleService;
import mutsa.api.service.user.UserService;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.order.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderModuleService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ArticleService articleService;

    public OrderDetailResponseDto findDetailOrder(String articleApiId, String orderUUID, String currentUserApiId) {
        User user = userService.getByApiId(currentUserApiId);
        Article article = articleService.getByApiId(articleApiId);
        Order order = getByApiId(orderUUID);

        order.validArticle(article);
        order.validUser(user); //판매자와 구매자만 확인할 수 있도록
        return OrderDetailResponseDto.fromEntity(order);
    }

    public List<OrderResponseDto> findAllOrder(String articleApiId, String currentUserApiId) {
        User user = userService.getByApiId(currentUserApiId);
        Article article = articleService.getByApiId(articleApiId);

        article.validUser(user); //판매자만 확인할 수 있는지
        return article.getOrders().stream().map(OrderResponseDto::fromEntity).collect(Collectors.toList());
    }

    public OrderDetailResponseDto saveOrder(String articleApiId, String currentUserApiId) {
        User user = userService.getByApiId(currentUserApiId);
        Article article = articleService.getByApiId(articleApiId);

        return OrderDetailResponseDto.fromEntity(orderRepository.save(Order.of(article, user)));
    }

    public void deleteOrder(String articleApiId, String orderApiId, String currentUserApiId) {
        User user = userService.getByApiId(currentUserApiId);
        Article article = articleService.getByApiId(articleApiId);
        Order order = getByApiId(orderApiId);

        order.validArticle(article);
        order.validUser(user); //판매자와 구매자만
        orderRepository.delete(getByApiId(orderApiId));
    }

    public Order getByApiId(String apiId) {
        Optional<Order> byApiId = orderRepository.findByApiId(apiId);
        if (byApiId.isPresent()) {
            return byApiId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 order ApiId");
    }

    public Order getById(Long id) {
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 order ID");
    }
}
