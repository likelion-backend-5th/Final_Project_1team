package mutsa.api.service.auth;

import lombok.RequiredArgsConstructor;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("not found username=" + username));

        return CustomPrincipalDetails.of(user, null);
    }
}
