package net.lelyak.init;

import lombok.extern.slf4j.Slf4j;
import net.lelyak.utils.TimeClock;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Nazar Lelyak.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class TestProfileConfigurer implements ApplicationListener<ApplicationPreparedEvent> {

    private static final ZonedDateTime TEST_PROFILER_DATE_MOCK = ZonedDateTime.of(
            LocalDateTime.of(2020, 2, 29, 12, 30, 0),
            ZoneId.of("UTC")
    );

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        if (environment.acceptsProfiles(Profiles.of("test"))) {
            log.info("Application is started in TEST profile");

            TimeClock.setDateTime(TEST_PROFILER_DATE_MOCK);
        }
    }
}
