package com.cinema.halls.application.queries.handlers;

import com.cinema.halls.application.queries.GetSeatBySeatId;
import com.cinema.halls.application.queries.dto.SeatDto;
import com.cinema.halls.domain.SeatRepository;
import com.cinema.halls.domain.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatByIdHandler {

    private final SeatRepository seatRepository;

    public SeatDto handle(GetSeatBySeatId query) {
        log.info("Query:{}", query);
        return seatRepository
                .getById(query.seatId())
                .map(seat -> new SeatDto(seat.getId(), seat.getRowNumber(), seat.getNumber()))
                .orElseThrow(SeatNotFoundException::new);
    }
}
