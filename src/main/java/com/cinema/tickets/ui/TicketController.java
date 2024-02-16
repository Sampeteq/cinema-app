package com.cinema.tickets.ui;

import com.cinema.tickets.application.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping("/admin/tickets")
    @SecurityRequirement(name = "basic")
    void addTickets(@RequestParam Long screeningId) {
        ticketService.addTickets(screeningId);
    }

    @PostMapping("/tickets/book")
    @SecurityRequirement(name = "basic")
    void bookTickets(@RequestBody @Valid TicketBookRequest request) {
        ticketService.bookTickets(request.screeningId(), request.seats());
    }

    @PatchMapping("/tickets/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    void cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
    }

    @GetMapping("/tickets/my")
    @SecurityRequirement(name = "basic")
    List<TicketView> getAllTicketsByLoggedUser() {
        return ticketService
                .getAllTicketsByLoggedUser()
                .stream()
                .map(ticketMapper::mapToView)
                .toList();
    }

    @GetMapping("/public/tickets")
    List<TicketView> getAllByScreeningId(@RequestParam Long screeningId) {
        return ticketService
                .getAllTicketsByScreeningId(screeningId)
                .stream()
                .map(ticketMapper::mapToView)
                .toList();
    }
}
