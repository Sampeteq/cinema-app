package com.cinema.screenings.application.handlers;

import com.cinema.films.application.handlers.ReadFilmHandler;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningMapper;
import com.cinema.screenings.application.queries.ReadScreening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadScreeningHandler {

    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final ReadFilmHandler readFilmHandler;

    public ScreeningDto handle(ReadScreening query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readById(query.id())
                .map(screening -> {
                    var readFilm = new ReadFilm(screening.getFilmId());
                    var filmDto = readFilmHandler.handle(readFilm);
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
