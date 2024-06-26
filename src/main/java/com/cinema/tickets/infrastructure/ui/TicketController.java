package com.cinema.tickets.infrastructure.ui;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketUserDto;
import com.cinema.tickets.application.TicketService;
import com.cinema.users.application.UserService;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    @PostMapping("/admin/tickets")
    @SecurityRequirement(name = "basic")
    void addTickets(@RequestParam UUID screeningId) {
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
    void cancelTicket(@PathVariable UUID ticketId, Principal principal) {
        var user = userService.getByMail(principal.getName());
        ticketService.cancelTicket(ticketId, user);
    }

    @GetMapping("/tickets/my")
    @SecurityRequirement(name = "basic")
    List<TicketUserDto> getAllTicketsByLoggedUser(Principal principal) {
        var user = userService.getByMail(principal.getName());
        return ticketService.getAllTicketsByUserId(user.getId());
    }

    @GetMapping("/public/tickets")
    List<TicketDto> getAllByScreeningId(@RequestParam UUID screeningId) {
        return ticketService.getAllTicketsByScreeningId(screeningId);
    }
}
