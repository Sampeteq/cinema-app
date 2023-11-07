package com.cinema.screenings.application.queries.handlers;

import com.cinema.films.application.queries.handlers.GetFilmHandler;
import com.cinema.films.application.queries.GetFilm;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.dto.ScreeningMapper;
import com.cinema.screenings.application.queries.GetScreeningsBy;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetScreeningsByHandler {

    private final ScreeningRepository screeningRepository;
    private final GetFilmHandler getFilmHandler;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(GetScreeningsBy query) {
        log.info("Query:{}", query);
        return screeningRepository
                .getAllBy(query)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screening -> {
                    var getFilm = new GetFilm(screening.getFilmId());
                    var filmDto = getFilmHandler.handle(getFilm);
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .toList();
    }
}
