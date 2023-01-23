package code.screenings;

import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

sealed interface ScreeningDateSpecification permits CurrentOrNextOneYearScreeningDateSpecification {
    boolean isSatisfyBy(LocalDateTime date);
}

@AllArgsConstructor
final class CurrentOrNextOneYearScreeningDateSpecification implements ScreeningDateSpecification {

    private final Clock clock;

    @Override
    public boolean isSatisfyBy(LocalDateTime date) {
        var currentDate = LocalDateTime.ofInstant(this.clock.instant(), ZoneOffset.UTC);
        var currentYear = currentDate.getYear();
        return date.getYear() == currentYear || date.getYear() == currentYear + 1;
    }
}
