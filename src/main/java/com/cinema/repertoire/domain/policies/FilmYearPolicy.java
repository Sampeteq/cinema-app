package com.cinema.repertoire.domain.policies;

import com.cinema.repertoire.domain.exceptions.FilmYearOutOfRangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Year;

@Component
@RequiredArgsConstructor
public class FilmYearPolicy {

    private final Clock clock;

    public void checkFilmYear(int filmYear) {
        var currentYear = Year.now(clock).getValue();
        var isFilmYearOfRange = filmYear < currentYear - 1 || filmYear > currentYear + 1;
        if (isFilmYearOfRange) {
            throw new FilmYearOutOfRangeException();
        }
    }
}
