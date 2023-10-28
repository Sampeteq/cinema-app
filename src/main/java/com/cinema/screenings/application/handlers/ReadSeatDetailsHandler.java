package com.cinema.screenings.application.handlers;

import com.cinema.screenings.application.dto.SeatDetailsDto;
import com.cinema.screenings.application.queries.ReadSeatDetails;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadSeatDetailsHandler {

    private final ScreeningRepository screeningRepository;

    public SeatDetailsDto handle(ReadSeatDetails query) {
        log.info("Query:{}", query);
        var seat = screeningRepository
                .readById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(query.seatId())
                .orElseThrow(SeatNotFoundException::new);
        return new SeatDetailsDto(
                seat.getRowNumber(),
                seat.getNumber()
        );
    }
}
