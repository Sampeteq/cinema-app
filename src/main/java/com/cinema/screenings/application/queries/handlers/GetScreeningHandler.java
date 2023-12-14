package com.cinema.screenings.application.queries.handlers;

import com.cinema.films.application.queries.GetFilm;
import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.infrastructure.ScreeningMapper;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetScreeningHandler {

    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final GetFilmHandler getFilmHandler;

    public ScreeningDto handle(GetScreening query) {
        log.info("Query:{}", query);
        return screeningRepository
                .getById(query.id())
                .map(screening -> {
                    var filmDto = getFilmHandler.handle(new GetFilm(screening.getFilmId()));
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .orElseThrow(ScreeningNotFoundException::new);
    }
}
