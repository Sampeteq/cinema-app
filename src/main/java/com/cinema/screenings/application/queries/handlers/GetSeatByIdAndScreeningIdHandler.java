package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.GetSeatByIdAndScreeningId;
import com.cinema.screenings.application.queries.dto.ScreeningSeatDto;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.screenings.infrastructure.ScreeningSeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatByIdAndScreeningIdHandler {

    private final ScreeningSeatRepository screeningSeatRepository;
    private final ScreeningSeatMapper screeningSeatMapper;

    public ScreeningSeatDto handle(GetSeatByIdAndScreeningId query) {
        log.info("Query:{}", query);
        var seat = screeningSeatRepository
                .getByIdAndScreeningId(query.seatId(), query.screeningId())
                .orElseThrow(ScreeningSeatNotFoundException::new);
        return screeningSeatMapper.mapToDto(seat);
    }
}
