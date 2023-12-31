package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetSeatsByScreeningId;
import com.cinema.screenings.application.queries.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.infrastructure.ScreeningSeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatsByScreeningIdHandler {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final ScreeningSeatMapper screeningSeatMapper;

    public List<ScreeningSeatDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        return screeningSeatRepository
                .getAllByScreeningId(query.screeningId())
                .stream()
                .map(screeningSeatMapper::mapToDto)
                .toList();
    }
}
