package mutsa.api.service.user;

import lombok.RequiredArgsConstructor;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserModuleService {
    private final UserRepository userRepository;

    public User getByApiId(String uuid) {
        return userRepository.findByApiId(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 user ApiId"));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 user ID"));
    }
}
