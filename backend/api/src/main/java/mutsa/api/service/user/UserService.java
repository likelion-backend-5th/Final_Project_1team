package mutsa.api.service.user;

import lombok.RequiredArgsConstructor;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserModuleService userModuleService;

    public User getByApiId(String apiId) {
        return userModuleService.getByApiId(apiId);
    }

    public User getById(Long id) {
        return userModuleService.getById(id);
    }
}