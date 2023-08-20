package mutsa.api.service.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.user.SignUpUserDto;
import mutsa.common.domain.models.user.Role;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.domain.models.user.UserRole;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.user.RoleRepository;
import mutsa.common.repository.user.UserRepository;
import mutsa.common.repository.user.UserRoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserModuleService userModuleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpUserDto signUpUserDto) {
        Optional<User> user = userRepository.findByUsername(signUpUserDto.getUsername());
        if (user.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATION_USER);
        }
        signUpUserDto.setPassword(bCryptPasswordEncoder.encode(signUpUserDto.getPassword()));

        User newUser = SignUpUserDto.from(signUpUserDto);
        Role role = roleRepository.findByValue(RoleStatus.ROLE_USER)
            .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ROLE));

        UserRole userRole = UserRole.of(newUser, role);
        userRole.addUser(newUser);

        userRepository.save(newUser);
        userRoleRepository.save(userRole);
    }
}