package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetSeatIdByHallIdAndPosition;
import com.cinema.halls.domain.SeatRepository;
import com.cinema.halls.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatIdByHallIdAndPositionHandler {

    private final SeatRepository seatRepository;

    public Long handle(GetSeatIdByHallIdAndPosition query) {
        log.info("Query:{}", query);
        return seatRepository
                .getByHallIdAndPosition(query.hallId(), query.rowNumber(), query.number())
                .orElseThrow(SeatNotFoundException::new)
                .getId();
    }
}
