package com.cinema.films.domain;

import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmFactory {

    private final FilmRepository filmRepository;

    public Film createFilm(String title, FilmCategory category, int year, int durationInMinutes) {
        if (filmRepository.findByTitle(title).isPresent()) {
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
