package com.cinema.screenings.application.handlers;

import com.cinema.films.application.handlers.ReadFilmHandler;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.screenings.application.dto.ScreeningDetailsDto;
import com.cinema.screenings.application.queries.ReadScreeningsDetails;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadScreeningsDetailsHandler {

    private final ScreeningRepository screeningRepository;
    private final ReadFilmHandler readFilmHandler;

    public ScreeningDetailsDto handle(ReadScreeningsDetails query) {
        var screening = screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new);
        var readFilmQuery = new ReadFilm(screening.getFilmId());
        var filmDto = readFilmHandler.handle(readFilmQuery);
        return new ScreeningDetailsDto(
                screening.getDate(),
                filmDto.title(),
                screening.getRoomId()
        );
    }
}
