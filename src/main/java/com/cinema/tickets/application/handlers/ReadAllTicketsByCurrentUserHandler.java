package com.cinema.tickets.application.handlers;

import com.cinema.screenings.application.handlers.ReadScreeningsDetailsHandler;
import com.cinema.screenings.application.handlers.ReadSeatDetailsHandler;
import com.cinema.screenings.application.queries.ReadScreeningsDetails;
import com.cinema.screenings.application.queries.ReadSeatDetails;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.queries.ReadAllTicketsByCurrentUser;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.handlers.ReadCurrentUserIdHandler;
import com.cinema.users.application.queries.ReadCurrentUserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReadAllTicketsByCurrentUserHandler {

    private final ReadCurrentUserIdHandler readCurrentUserIdHandler;
    private final TicketRepository ticketRepository;
    private final ReadScreeningsDetailsHandler readScreeningsDetailsHandler;
    private final ReadSeatDetailsHandler readSeatDetailsHandler;

    public List<TicketDto> handle(ReadAllTicketsByCurrentUser query) {
        log.info("Query:{}", query);
        var readCurrentUserIdQuery = new ReadCurrentUserId();
        var currentUserId = readCurrentUserIdHandler.handle(readCurrentUserIdQuery);
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticket -> {
                    var readScreeningsDetails = new ReadScreeningsDetails(ticket.getScreeningId());
                    var screeningDetails = readScreeningsDetailsHandler.handle(readScreeningsDetails);
                    var readSeatDetails = new ReadSeatDetails(ticket.getScreeningId(), ticket.getSeatId());
                    var seatDetails = readSeatDetailsHandler.handle(readSeatDetails);
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
