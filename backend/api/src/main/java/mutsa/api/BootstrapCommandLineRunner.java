package mutsa.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.BootstrapDataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class BootstrapCommandLineRunner implements CommandLineRunner {
    private final BootstrapDataLoader bootStrapDataLoader;

    @Override
    public void run(String... args) throws Exception {
        bootStrapDataLoader.createAdminUser();
//        bootStrapDataLoader.createAricleOrder();
    }
}
