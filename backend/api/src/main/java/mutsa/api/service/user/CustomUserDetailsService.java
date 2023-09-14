package mutsa.api.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.config.security.CustomPrincipalDetails;
import mutsa.common.domain.models.user.User;
import mutsa.common.repository.cache.UserCacheRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;

    @Override
    public CustomPrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : {}", username);
        User user = userCacheRepository.getUser(username).orElseGet(() ->
                userRepository.findByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException("not found username:" + username)));
        return CustomPrincipalDetails.of(user, null);

    }
}
