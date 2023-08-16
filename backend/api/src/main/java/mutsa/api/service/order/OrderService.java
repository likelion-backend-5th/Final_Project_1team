package mutsa.api.service.order;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderService {
    private final OrderModuleService orderModuleService;

    public OrderDetailResponseDto findDetailOrder(String articleApiId, String orderUUID, String currentUserApiId) {
        return orderModuleService.findDetailOrder(articleApiId, orderUUID, currentUserApiId);
    }

    public List<OrderResponseDto> findAllOrder(String articleApiId, String currentUserApiId) {
        return orderModuleService.findAllOrder(articleApiId, currentUserApiId);
    }

    public OrderDetailResponseDto saveOrder(String articleApiId, String currentUserApiId) {
        return orderModuleService.saveOrder(articleApiId, currentUserApiId);
    }

    public void deleteOrder(String articleApiId, String orderApiId, String currentUserApiId) {
        orderModuleService.deleteOrder(articleApiId,orderApiId,currentUserApiId);
    }

    public Order getByApiId(String apiId) {
        return orderModuleService.getByApiId(apiId);
    }

    public Order getById(Long id) {
        return orderModuleService.getById(id);
    }
}
