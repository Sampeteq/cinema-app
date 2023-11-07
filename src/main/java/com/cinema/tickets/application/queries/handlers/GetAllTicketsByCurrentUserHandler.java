package com.cinema.tickets.application.queries.handlers;

import com.cinema.screenings.application.queries.handlers.GetScreeningHandler;
import com.cinema.screenings.application.queries.handlers.GetSeatHandler;
import com.cinema.screenings.application.queries.GetScreening;
import com.cinema.screenings.application.queries.GetSeat;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.application.queries.GetAllTicketsByCurrentUser;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.queries.handlers.GetCurrentUserIdHandler;
import com.cinema.users.application.queries.GetCurrentUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAllTicketsByCurrentUserHandler {

    private final GetCurrentUserIdHandler getCurrentUserIdHandler;
    private final TicketRepository ticketRepository;
    private final GetScreeningHandler getScreeningHandler;
    private final GetSeatHandler getSeatHandler;

    public List<TicketDto> handle(GetAllTicketsByCurrentUser query) {
        log.info("Query:{}", query);
        var getCurrentUserIdQuery = new GetCurrentUserId();
        var currentUserId = getCurrentUserIdHandler.handle(getCurrentUserIdQuery);
        return ticketRepository
                .getAllByUserId(currentUserId)
                .stream()
                .map(ticket -> {
                    var getScreening = new GetScreening(ticket.getScreeningId());
                    var screeningDto = getScreeningHandler.handle(getScreening);
                    var getSeat = new GetSeat(ticket.getScreeningId(), ticket.getSeatId());
                    var seatDto = getSeatHandler.handle(getSeat);
                    return new TicketDto(
                            ticket.getId(),
                            ticket.getStatus(),
                            screeningDto.filmTitle(),
                            screeningDto.date(),
                            screeningDto.roomId(),
                            seatDto.rowNumber(),
                            seatDto.number()
                    );
                })
                .toList();
    }
}
