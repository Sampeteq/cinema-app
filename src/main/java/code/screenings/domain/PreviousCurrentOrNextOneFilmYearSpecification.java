package code.screenings.domain;

import java.time.Year;

public final class PreviousCurrentOrNextOneFilmYearSpecification implements FilmYearSpecification {

    private static final int currentYear = Year.now().getValue();

    @Override
    public boolean isSatisfyBy(int year) {
        return year == currentYear - 1 || year == currentYear || year == currentYear + 1;
    }
}
