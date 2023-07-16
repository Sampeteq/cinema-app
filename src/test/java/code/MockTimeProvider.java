package code;

import code.shared.TimeProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Year;

@Component
@Profile("test")
public class MockTimeProvider implements TimeProvider {

    private static final int currentYear = Year.now().getValue();

    @Override
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.of(currentYear, 12, 13, 16, 30);
    }
}
