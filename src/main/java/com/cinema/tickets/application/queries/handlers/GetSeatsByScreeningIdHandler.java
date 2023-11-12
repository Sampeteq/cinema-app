package com.cinema.tickets.application.queries.handlers;

import com.cinema.tickets.application.queries.GetSeatsByScreeningId;
import com.cinema.tickets.application.queries.dto.SeatDto;
import com.cinema.tickets.application.queries.dto.SeatMapper;
import com.cinema.tickets.domain.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatsByScreeningIdHandler {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    public List<SeatDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        return seatRepository
                .getAllByScreeningId(query.screeningId())
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}
