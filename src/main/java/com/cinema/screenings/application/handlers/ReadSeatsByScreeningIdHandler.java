package com.cinema.screenings.application.handlers;

import com.cinema.screenings.application.dto.SeatDto;
import com.cinema.screenings.application.dto.SeatMapper;
import com.cinema.screenings.application.queries.ReadSeatsByScreeningId;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadSeatsByScreeningIdHandler {

    private final ScreeningRepository screeningRepository;
    private final SeatMapper seatMapper;

    public List<SeatDto> handle(ReadSeatsByScreeningId query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeats()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}
