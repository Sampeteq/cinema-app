package com.cinema.tickets.application.handlers;

import com.cinema.screenings.application.services.ScreeningService;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.queries.ReadAllTicketsByCurrentUser;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadAllTicketsByCurrentUserHandler {

    private final UserService userService;
    private final TicketRepository ticketRepository;
    private final ScreeningService screeningService;

    public List<TicketDto> handle(ReadAllTicketsByCurrentUser query) {
        log.info("Query:{}", query);
        var currentUserId = userService.readCurrentUserId();
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticket -> {
                    var screeningDetails = screeningService.readScreeningDetails(
                            ticket.getScreeningId()
                    );
                    var seatDetails = screeningService.readSeatDetails(
                            ticket.getScreeningId(),
                            ticket.getSeatId()
                    );
                    return new TicketDto(
                            ticket.getId(),
                            ticket.getStatus(),
                            screeningDetails.filmTitle(),
                            screeningDetails.date(),
                            screeningDetails.roomId(),
                            seatDetails.rowNumber(),
                            seatDetails.seatNumber()
                    );
                })
                .toList();
    }
}
