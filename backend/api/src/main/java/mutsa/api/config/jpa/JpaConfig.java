package mutsa.api.config.jpa;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableJpaRepositories("mutsa.common")
public class JpaConfig {
}
