package com.cinema.screenings.application.queries.handlers;

import com.cinema.screenings.application.queries.dto.SeatWithStatusDto;
import com.cinema.halls.domain.SeatRepository;
import com.cinema.screenings.application.queries.GetSeatsByScreeningId;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.tickets.application.queries.GetTicketsSeatIdsByScreeningId;
import com.cinema.tickets.application.queries.handlers.GetTicketsSeatIdsByScreeningHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetSeatsByScreeningIdHandler {

    private final ScreeningRepository screeningRepository;
    private final GetTicketsSeatIdsByScreeningHandler getTicketsSeatIdsByScreeningHandler;
    private final SeatRepository seatRepository;

    public List<SeatWithStatusDto> handle(GetSeatsByScreeningId query) {
        log.info("Query:{}", query);
        var hallId = screeningRepository
                .getById(query.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getHallId();
        var ticketsSeatsId = getTicketsSeatIdsByScreeningHandler.handle(
                new GetTicketsSeatIdsByScreeningId(query.screeningId())
        );
        return seatRepository
                .getAllByHallId(hallId)
                .stream()
                .map(seat -> new SeatWithStatusDto(
                        seat.getRowNumber(),
                        seat.getNumber(),
                        !ticketsSeatsId.contains(seat.getId())
                )).toList();
    }
}
