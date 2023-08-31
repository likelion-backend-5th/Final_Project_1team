package mutsa.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.common.BootstrapDataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class BootstrapCommandLineRunner implements CommandLineRunner {
    private final BootstrapDataLoader bootStrapDataLoader;
    @Value("${dataloader}")
    private String dataloader;


    @Override
    public void run(String... args) throws Exception {
        if(dataloader.equals("1"))
        bootStrapDataLoader.createAdminUser();
        bootStrapDataLoader.createAricleOrder();
        bootStrapDataLoader.createReport();
    }
}
