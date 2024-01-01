package com.cinema.tickets.application.queries.handlers;

import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.GetSeatByIdAndScreeningId;
import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.screenings.application.queries.handlers.GetSeatByIdAndScreeningIdHandler;
import com.cinema.tickets.application.queries.GetAllTicketsByLoggedUser;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.queries.GetLoggedUserId;
import com.cinema.users.application.queries.handlers.GetLoggedUserIdHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllTicketsByLoggedUserHandler {

    private final GetLoggedUserIdHandler getLoggedUserIdHandler;
    private final TicketRepository ticketRepository;
    private final GetScreeningHandler getScreeningHandler;
    private final GetSeatByIdAndScreeningIdHandler getSeatByIdAndScreeningIdHandler;

    public List<TicketDto> handle(GetAllTicketsByLoggedUser query) {
        log.info("Query:{}", query);
        var loggedUserId = getLoggedUserIdHandler.handle(new GetLoggedUserId());
        return ticketRepository
                .getAllByUserId(loggedUserId)
                .stream()
                .map(ticket -> {
                    var getScreening = new GetScreening(ticket.getScreeningId());
                    var screeningDto = getScreeningHandler.handle(getScreening);
                    var seatDto = getSeatByIdAndScreeningIdHandler.handle(
                            new GetSeatByIdAndScreeningId(ticket.getSeatId(), ticket.getScreeningId())
                    );
                    return new TicketDto(
                            ticket.getId(),
                            ticket.getStatus(),
                            screeningDto.filmTitle(),
                            screeningDto.date(),
                            screeningDto.hallId(),
                            seatDto.rowNumber(),
                            seatDto.number()
                    );
                })
                .toList();
    }
}
