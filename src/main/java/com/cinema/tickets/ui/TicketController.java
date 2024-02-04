package com.cinema.tickets.ui;

import com.cinema.tickets.application.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @SecurityRequirement(name = "basic")
    void bookTicket(@RequestBody @Valid BookTicketDto dto) {
        ticketService.bookTicket(dto.screeningId(), dto.seatsIds());
    }

    @PatchMapping("/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    void cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    TicketsResponse getAllTicketsByLoggedUser() {
        var tickets = ticketService.getAllTicketsByLoggedUser();
        return new TicketsResponse(tickets);
    }
}
