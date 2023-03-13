package code.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;

@Configuration
public class SharedBeansConfig {

    @Bean
    @Profile("prod")
    public Clock prodClock() {
        return Clock.systemUTC();
    }

    @Bean
    @Profile("test")
    public Clock testClock() {
        var currentYear = Year.now().getValue();
        var testDateAsInstant = LocalDateTime
                .of(currentYear, 5, 8, 18, 30)
                .toInstant(ZoneOffset.UTC);
        return Clock.fixed(
                testDateAsInstant,
                ZoneOffset.UTC
        );
    }
}
