package com.cinema.tickets.infrastructure.ui;

import com.cinema.tickets.domain.TicketDto;
import com.cinema.tickets.domain.TicketService;
import com.cinema.users.domain.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    @PostMapping("/admin/tickets")
    @SecurityRequirement(name = "basic")
    void addTickets(@RequestParam long screeningId) {
        ticketService.addTickets(screeningId);
    }

    @PostMapping("/tickets/book")
    @SecurityRequirement(name = "basic")
    void bookTickets(@RequestBody @Valid TicketBookDto ticketBookDto, Principal principal) {
        var user = userService.getByMail(principal.getName());
        ticketService.bookTickets(ticketBookDto.screeningId(), ticketBookDto.seats(), user);
    }

    @PatchMapping("/tickets/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    void cancelTicket(@PathVariable long ticketId, Principal principal) {
        var user = userService.getByMail(principal.getName());
        ticketService.cancelTicket(ticketId, user);
    }

    @GetMapping("/tickets/my")
    @SecurityRequirement(name = "basic")
    List<TicketDto> getAllTicketsByLoggedUser(Principal principal) {
        var user = userService.getByMail(principal.getName());
        return ticketService.getAllTicketsByUserId(user.getId());
    }

    @GetMapping("/public/tickets")
    List<TicketDto> getAllByScreeningId(@RequestParam long screeningId) {
        return ticketService.getAllTicketsByScreeningId(screeningId);
    }
}
