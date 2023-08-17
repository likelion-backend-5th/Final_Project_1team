package mutsa.api.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserModuleService userModuleService;

}