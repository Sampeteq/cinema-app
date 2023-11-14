package com.cinema.films.domain;

import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.exceptions.FilmYearOutOfRangeException;
import com.cinema.films.domain.specifications.FilmYearSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmFactory {

    private final FilmYearSpecification filmYearSpecification;
    private final FilmRepository filmRepository;

    public Film createFilm(String title, FilmCategory category, int year, int durationInMinutes) {
        if (!filmYearSpecification.isFilmYearPreviousCurrentOrNext(year)) {
            throw new FilmYearOutOfRangeException();
        }
        if (filmRepository.existsByTitle(title)) {
            throw new FilmTitleNotUniqueException();
        }
        return new Film(
                title,
                category,
                year,
                durationInMinutes
        );
    }
}
