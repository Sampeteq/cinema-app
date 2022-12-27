package code.films;

import java.time.Year;

sealed interface FilmYearSpecification permits PreviousCurrentOrNextOneFilmYearSpecification {
    boolean isSatisfyBy(int year);
}

final class PreviousCurrentOrNextOneFilmYearSpecification implements FilmYearSpecification {

    private static final int currentYear = Year.now().getValue();

    @Override
    public boolean isSatisfyBy(int year) {
        return year == currentYear - 1 || year == currentYear || year == currentYear + 1;
    }
}


