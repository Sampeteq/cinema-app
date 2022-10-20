package code.films;

import code.films.exception.FilmYearException;
import lombok.*;

import javax.persistence.Embeddable;
import java.time.Year;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
class FilmYear {

    private int value;

    static FilmYear of(int year) {
        var currentYear = Year
                .now()
                .getValue();
        if (year != currentYear - 1 && year != currentYear && year != currentYear + 1) {
            throw new FilmYearException(year);
        } else {
            return new FilmYear(year);
        }
    }
}
