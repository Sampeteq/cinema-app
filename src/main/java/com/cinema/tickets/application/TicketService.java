package com.cinema.tickets.application;

import com.cinema.screenings.application.ScreeningSeatService;
import com.cinema.screenings.application.ScreeningService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketBooked;
import com.cinema.tickets.domain.TicketBookingPolicy;
import com.cinema.tickets.domain.TicketCancelled;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import com.cinema.users.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketBookingPolicy ticketBookingPolicy;
    private final TicketCancellingPolicy ticketCancellingPolicy;
    private final ScreeningService screeningService;
    private final ScreeningSeatService screeningSeatService;
    private final UserService userService;
    private final EventPublisher eventPublisher;

    @Transactional
    public void bookTicket(BookTicketDto dto) {
        log.info("Dto:{}", dto);
        var timeToScreening = screeningService.getTimeToScreeningInHours(dto.screeningId());
        log.info("Time to screening in hours:{}", timeToScreening);
        ticketBookingPolicy.checkScreeningDate(timeToScreening);
        var loggedUserId = userService.getLoggedUserId();
        dto
                .seatsIds()
                .stream()
                .map(seatId -> {
                    var seatDto = screeningSeatService.getSeatByIdAndScreeningId(seatId, dto.screeningId());
                    log.info("Found seat: {}", seatDto);
                    if (ticketRepository.existsBySeatId(seatDto.id())) {
                        throw new TicketAlreadyExistsException();
                    }
                    return new Ticket(TicketStatus.BOOKED, dto.screeningId(), seatDto.id(), loggedUserId);
                })
                .toList()
                .stream()
                .map(ticket -> {
                    var addedTicket = ticketRepository.add(ticket);
                    log.info("Added ticket:{}", addedTicket);
                    return addedTicket;
                })
                .toList()
                .forEach(addedTicket -> {
                    var event = new TicketBooked(addedTicket.getSeatId());
                    eventPublisher.publish(event);
                    log.info("Published event:{}", event);
                });
    }

    @Transactional
    public void cancelTicket(Long ticketId) {
        log.info("Ticket id:{}", ticketId);
        var loggedUserId = userService.getLoggedUserId();
        var ticket = ticketRepository
                .getByIdAndUserId(ticketId, loggedUserId)
                .orElseThrow(TicketNotFoundException::new);
        log.info("Found ticket:{}", ticket);
        var timeToScreeningInHours = screeningService.getTimeToScreeningInHours(ticket.getScreeningId());
        log.info("Time to screening in hours:{}", timeToScreeningInHours);
        ticketCancellingPolicy.checkScreeningDate(timeToScreeningInHours);
        ticket.cancel();
        log.info("Ticket cancelled:{}", ticket);
        var event = new TicketCancelled(ticket.getSeatId());
        eventPublisher.publish(event);
        log.info("Published event:{}", event);
    }

    public List<TicketDto> getAllTicketsByLoggedUser() {
        var loggedUserId = userService.getLoggedUserId();
        return ticketRepository
                .getAllByUserId(loggedUserId)
                .stream()
                .map(ticket -> {
                    var screeningDto = screeningService.getScreeningById(ticket.getId());
                    var seatDto = screeningSeatService.getSeatByIdAndScreeningId(
                            ticket.getSeatId(),
                            ticket.getScreeningId()
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
