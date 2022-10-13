package code.film.exception;

import java.time.Year;

public class FilmYearException extends FilmException {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public FilmYearException(int year) {
        super(
                "A film year must be previous, current or next one."
                        + "Given: " + year
                        + ".Previous: " + (CURRENT_YEAR - 1)
                        + ".Current: " + CURRENT_YEAR
                        + ".Next: " + (CURRENT_YEAR + 1)
        );
    }
}
