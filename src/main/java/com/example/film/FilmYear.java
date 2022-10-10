package com.example.film;

import com.example.film.exception.WrongFilmYearException;
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
            throw new WrongFilmYearException(year);
        } else {
            return new FilmYear(year);
        }
    }
}
