package com.cinema.screenings.application.queries.handlers;

import com.cinema.films.application.FilmApi;
import com.cinema.screenings.application.queries.GetScreenings;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.dto.ScreeningMapper;
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
public class GetScreeningsHandler {

    private final ScreeningRepository screeningRepository;
    private final FilmApi filmApi;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(GetScreenings query) {
        log.info("Query:{}", query);
        return screeningRepository
                .getAll(query)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screening -> {
                    var filmDto = filmApi.getFilmById(screening.getFilmId());
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .toList();
    }
}
