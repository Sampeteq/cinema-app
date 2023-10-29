package com.cinema.screenings.application.queries.handlers;

import com.cinema.films.application.queries.handlers.ReadFilmHandler;
import com.cinema.films.application.queries.ReadFilm;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.dto.ScreeningMapper;
import com.cinema.screenings.application.queries.ReadScreeningsBy;
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
public class ReadScreeningsByHandler {

    private final ScreeningRepository screeningRepository;
    private final ReadFilmHandler readFilmHandler;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(ReadScreeningsBy query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readAllBy(query)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screening -> {
                    var readFilm = new ReadFilm(screening.getFilmId());
                    var filmDto = readFilmHandler.handle(readFilm);
                    return screeningMapper.mapToDto(screening, filmDto.title());
                })
                .toList();
    }
}
