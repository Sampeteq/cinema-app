package com.cinema.catalog.domain.services;

import com.cinema.catalog.domain.exceptions.FilmYearOutOfRangeException;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class FilmYearValidateService {

    public void validate(int filmYear) {
        if (!isFilmYearCorrect(filmYear)) {
            throw new FilmYearOutOfRangeException();
        }
    }

    private static boolean isFilmYearCorrect(Integer year) {
        var currentYear = Year.now().getValue();
        return year == currentYear - 1 || year == currentYear || year == currentYear + 1;
    }
}
