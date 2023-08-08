package mutsa.common.config.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@EnableJpaAuditing
@Component
public class AuditorConfig implements AuditorAware<String> {
    private static final String ANONYMOUS = "anonymous";

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return Optional.of(ANONYMOUS);
        }
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        return Optional.of(username);
    }
}
