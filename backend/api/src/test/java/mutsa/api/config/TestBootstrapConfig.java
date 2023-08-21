package mutsa.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.BootstrapDataLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TestBootstrapConfig {
    private final BootstrapDataLoader bootstrapDataLoader;

    @PostConstruct
    public void init() {
        bootstrapDataLoader.createAdminUser();
    }
}
