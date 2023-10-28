package com.cinema.films.application.handlers;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.films.domain.policies.FilmYearPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateFilmHandler {

    private final FilmYearPolicy filmYearPolicy;
    private final FilmRepository filmRepository;

    public void handle(CreateFilm command) {
        log.info("Command:{}", command);
        filmYearPolicy.checkFilmYear(command.year());
        if (filmRepository.existsByTitle(command.title())) {
            throw new FilmTitleNotUniqueException();
        }
        var film = new Film(
                command.title(),
                command.category(),
                command.year(),
                command.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        log.info("Added film:{}", addedFilm);
    }
}
