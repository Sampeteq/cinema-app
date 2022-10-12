package code.film.exception;

import java.time.Year;

public class WrongFilmYearException extends FilmException {

    private static final int CURRENT_YEAR = Year.now().getValue();

    public WrongFilmYearException(int year) {
        super(
                "A film year must be previous, current or next one."
                        + "Given: " + year
                        + ".Previous: " + (CURRENT_YEAR - 1)
                        + ".Current: " + CURRENT_YEAR
                        + ".Next: " + (CURRENT_YEAR + 1)
        );
    }
}
