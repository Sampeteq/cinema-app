package com.cinema.films.domain.specifications;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Year;

@Component
@RequiredArgsConstructor
public class FilmYearSpecification {

    private final Clock clock;

    public boolean isFilmYearPreviousCurrentOrNext(int filmYear) {
        var currentYear = Year.now(clock).getValue();
        return filmYear == currentYear - 1 || filmYear == currentYear || filmYear == currentYear + 1;
    }
}
