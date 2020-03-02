package net.lelyak.init;

import lombok.extern.slf4j.Slf4j;
import net.lelyak.utils.Clock;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

import java.time.LocalDateTime;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class TestProfileConfigurer implements ApplicationListener<ApplicationPreparedEvent> {

    private static final LocalDateTime TEST_DATE_MOCK = LocalDateTime.of(2020, 2, 29, 12, 30);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        if (environment.acceptsProfiles(Profiles.of("test"))) {
            log.info("Application is started in TEST profile");

            Clock.setDateTime(TEST_DATE_MOCK);
        }
    }
}
