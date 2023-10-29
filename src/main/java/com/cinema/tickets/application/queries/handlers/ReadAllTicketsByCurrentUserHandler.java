package com.cinema.tickets.application.queries.handlers;

import com.cinema.screenings.application.queries.handlers.ReadScreeningHandler;
import com.cinema.screenings.application.queries.handlers.ReadSeatHandler;
import com.cinema.screenings.application.queries.ReadScreening;
import com.cinema.screenings.application.queries.ReadSeat;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.application.queries.ReadAllTicketsByCurrentUser;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.application.queries.handlers.ReadCurrentUserIdHandler;
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
    private final ReadScreeningHandler readScreeningHandler;
    private final ReadSeatHandler readSeatHandler;

    public List<TicketDto> handle(ReadAllTicketsByCurrentUser query) {
        log.info("Query:{}", query);
        var readCurrentUserIdQuery = new ReadCurrentUserId();
        var currentUserId = readCurrentUserIdHandler.handle(readCurrentUserIdQuery);
        return ticketRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(ticket -> {
                    var readScreening = new ReadScreening(ticket.getScreeningId());
                    var screeningDto = readScreeningHandler.handle(readScreening);
                    var readSeat = new ReadSeat(ticket.getScreeningId(), ticket.getSeatId());
                    var seatDto = readSeatHandler.handle(readSeat);
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
