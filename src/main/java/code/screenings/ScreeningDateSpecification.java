package code.screenings;

import java.time.LocalDateTime;
import java.time.Year;

sealed interface ScreeningDateSpecification permits CurrentOrNextOneYearScreeningDateSpecification {
    boolean isSatisfyBy(LocalDateTime date);
}

final class CurrentOrNextOneYearScreeningDateSpecification implements ScreeningDateSpecification {

    private static final int currentYear = Year.now().getValue();

    @Override
    public boolean isSatisfyBy(LocalDateTime date) {
        return date.getYear() == currentYear || date.getYear() == currentYear + 1;
    }
}
