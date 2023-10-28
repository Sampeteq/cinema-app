package com.cinema.screenings.application.handlers;

import com.cinema.films.application.handlers.ReadFilmTitleHandler;
import com.cinema.films.application.queries.ReadFilmTitle;
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
    private final ReadFilmTitleHandler readFilmTitleHandler;

    public ScreeningDetailsDto handle(ReadScreeningsDetails query) {
        var screening = screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new);
        var readFilmTitleQuery = new ReadFilmTitle(screening.getFilmId());
        var filmTitle = readFilmTitleHandler.handle(readFilmTitleQuery);
        return new ScreeningDetailsDto(
                screening.getDate(),
                filmTitle,
                screening.getRoomId()
        );
    }
}
