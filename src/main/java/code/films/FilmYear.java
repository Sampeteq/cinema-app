package code.films;

import code.films.exception.FilmYearException;
import lombok.*;

import javax.persistence.Embeddable;
import java.time.Year;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
class FilmYear {

    private int value;

    static FilmYear of(int year) {
        if (isFilmYearPreviousCurrentOrNextOne(year)) {
            return new FilmYear(year);
        } else {
            throw new FilmYearException(year);
        }
    }

    private static boolean isFilmYearPreviousCurrentOrNextOne(int year) {
        var currentYear = Year.now().getValue();
        return year == currentYear - 1 || year == currentYear || year == currentYear + 1;
    }
}
