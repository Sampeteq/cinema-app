package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.dto.SeatDto;
import com.cinema.screenings.application.queries.dto.SeatMapper;
import com.cinema.screenings.application.queries.ReadSeat;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadSeatHandler {

    private final ScreeningRepository screeningRepository;
    private final SeatMapper seatMapper;

    public SeatDto handle(ReadSeat query) {
        log.info("Query:{}", query);
        return screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(query.seatId())
                .map(seatMapper::toDto)
                .orElseThrow(SeatNotFoundException::new);
    }
}
