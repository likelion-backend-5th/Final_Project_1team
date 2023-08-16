package mutsa.api.service.user;

import lombok.RequiredArgsConstructor;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserModuleService {
    private final UserRepository userRepository;

    public User getByApiId(String uuid) {
        Optional<User> byApiId = userRepository.findByApiId(uuid);
        if (byApiId.isPresent()) {
            return byApiId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 user ApiId");
    }

    public User getById(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 user ID");
    }
}
