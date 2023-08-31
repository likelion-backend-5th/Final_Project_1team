package mutsa.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.BootstrapDataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@TestConfiguration
@Slf4j
@Transactional
@RequiredArgsConstructor
@ActiveProfiles("test")
public class TestBootstrapConfig {
    private final BootstrapDataLoader bootstrapDataLoader;
    @Value("${dataloader}")
    private String dataloader;

    @PostConstruct
    public void init() {
        if (dataloader.equals("2")) {
            bootstrapDataLoader.createAdminUser();
        }
    }
}
