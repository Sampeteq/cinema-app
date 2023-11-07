package com.cinema.films.application.commands.handlers;

import com.cinema.films.application.commands.DeleteFilm;
import com.cinema.films.domain.FilmRepository;
import com.cinema.films.domain.exceptions.FilmNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteFilmHandler {

    private final FilmRepository filmRepository;

    public void handle(DeleteFilm command) {
        log.info("Command:{}", command);
        var film = filmRepository
                .getById(command.filmId())
                .orElseThrow(FilmNotFoundException::new);
        filmRepository.delete(film);
    }
}
