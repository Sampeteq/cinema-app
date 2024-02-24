package com.cinema.tickets;

import com.cinema.films.FilmService;
import com.cinema.screenings.ScreeningService;
import com.cinema.users.UserService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final ScreeningService screeningService;
    private final FilmService filmService;
    private final UserService userService;

    @PostMapping("/admin/tickets")
    @SecurityRequirement(name = "basic")
    void addTickets(@RequestParam Long screeningId) {
        ticketService.addTickets(screeningId);
    }

    @PostMapping("/tickets/book")
    @SecurityRequirement(name = "basic")
    void bookTickets(@RequestBody @Valid TicketBookRequest request) {
        var userId = userService.getLoggedUserId();
        ticketService.bookTickets(request.screeningId(), request.seats(), userId);
    }

    @PatchMapping("/tickets/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    void cancelTicket(@PathVariable Long ticketId) {
        var userId = userService.getLoggedUserId();
        ticketService.cancelTicket(ticketId, userId);
    }

    @GetMapping("/tickets/my")
    @SecurityRequirement(name = "basic")
    List<TicketView> getAllTicketsByLoggedUser() {
        var userId = userService.getLoggedUserId();
        return ticketService
                .getAllTicketsByUserId(userId)
                .stream()
                .map(this::mapTicketToView)
                .toList();
    }

    @GetMapping("/public/tickets")
    List<TicketView> getAllByScreeningId(@RequestParam Long screeningId) {
        return ticketService
                .getAllTicketsByScreeningId(screeningId)
                .stream()
                .map(this::mapTicketToView)
                .toList();
    }

    private TicketView mapTicketToView(Ticket ticket) {
        var screening = screeningService.getScreeningById(ticket.getScreeningId());
        var film = filmService.getFilmById(screening.getFilmId());
        return ticketMapper.mapToView(
                ticket,
                film.getTitle(),
                screening.getDate(),
                screening.getHallId()
        );
    }
}
