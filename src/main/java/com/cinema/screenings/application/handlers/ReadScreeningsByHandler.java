package com.cinema.screenings.application.handlers;

import com.cinema.films.application.handlers.ReadFilmTitleHandler;
import com.cinema.films.application.queries.ReadFilmTitle;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningMapper;
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
    private final ReadFilmTitleHandler readFilmTitleHandler;
    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(ReadScreeningsBy query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readAllBy(query)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screening -> {
                    var readFilmTitle = new ReadFilmTitle(screening.getFilmId());
                    var filmTitle = readFilmTitleHandler.handle(readFilmTitle);
                    return screeningMapper.mapToDto(screening, filmTitle);
                })
                .toList();
    }
}
