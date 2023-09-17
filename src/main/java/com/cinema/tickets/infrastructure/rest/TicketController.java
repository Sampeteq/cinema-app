package com.cinema.tickets.infrastructure.rest;

import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.services.TicketFacade;
import com.cinema.catalog.application.dto.SeatDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
class TicketController {

    private final TicketFacade ticketFacade;

    @PostMapping
    @SecurityRequirement(name = "basic")
    void bookTicket(@RequestBody @Valid TicketBookDto dto) {
        ticketFacade.bookTicket(dto);
    }

    @PostMapping("/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    void cancelTicket(@PathVariable Long ticketId) {
        ticketFacade.cancelTicket(ticketId);
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<TicketDto> readAllTickets() {
        return ticketFacade.readTicketsByCurrentUser();
    }

    @GetMapping("/seats")
    List<SeatDto> readSeatsByScreeningId(@RequestParam Long screeningId) {
        return ticketFacade.readSeatsByScreeningId(screeningId);
    }
}

