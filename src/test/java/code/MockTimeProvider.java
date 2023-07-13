package code;

import code.shared.TimeProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("test")
public class MockTimeProvider implements TimeProvider {

    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.of(2023, 12, 13, 16, 30);
    }
}
