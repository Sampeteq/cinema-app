package com.cinema.films.application.commands.handlers;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.films.domain.FilmFactory;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateFilmHandler {

    private final FilmFactory filmFactory;
    private final FilmRepository filmRepository;

    public void handle(CreateFilm command) {
        log.info("Command:{}", command);
        var film = filmFactory.createFilm(
                command.title(),
                command.category(),
                command.year(),
                command.durationInMinutes()
        );
        var addedFilm = filmRepository.add(film);
        log.info("Added film:{}", addedFilm);
    }
}
