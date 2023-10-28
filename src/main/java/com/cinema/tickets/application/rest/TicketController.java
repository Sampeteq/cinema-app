package com.cinema.tickets.application.rest;

import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.application.commands.CancelTicket;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.handlers.BookTicketHandler;
import com.cinema.tickets.application.handlers.CancelTicketHandler;
import com.cinema.tickets.application.handlers.ReadAllTicketsByCurrentUserHandler;
import com.cinema.tickets.application.queries.ReadAllTicketsByCurrentUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
class TicketController {

    private final BookTicketHandler bookTicketHandler;
    private final CancelTicketHandler cancelTicketHandler;
    private final ReadAllTicketsByCurrentUserHandler readAllTicketsByCurrentUserHandler;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> bookTicket(@RequestBody @Valid BookTicket command) {
        log.info("Command:{}", command);
        bookTicketHandler.handle(command);
        var responseEntity = ResponseEntity.created(URI.create("/my/tickets")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @PatchMapping("/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> cancelTicket(@PathVariable Long ticketId) {
        log.info("Ticket id:{}", ticketId);
        var command = new CancelTicket(ticketId);
        cancelTicketHandler.handle(command);
        var responseEntity = ResponseEntity.ok().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<TicketDto> readAllTicketsByCurrentUser() {
        var query = new ReadAllTicketsByCurrentUser();
        return readAllTicketsByCurrentUserHandler.handle(query);
    }
}

