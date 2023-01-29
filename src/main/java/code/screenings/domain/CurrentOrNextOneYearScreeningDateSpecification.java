package code.screenings.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@AllArgsConstructor
public final class CurrentOrNextOneYearScreeningDateSpecification implements ScreeningDateSpecification {

    private final Clock clock;

    @Override
    public boolean isSatisfyBy(LocalDateTime date) {
        var currentDate = LocalDateTime.ofInstant(this.clock.instant(), ZoneOffset.UTC);
        var currentYear = currentDate.getYear();
        return date.getYear() == currentYear || date.getYear() == currentYear + 1;
    }
}
