package com.cinema.tickets.application.rest;

import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.services.TicketService;
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

    private final TicketService ticketService;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> bookTicket(@RequestBody @Valid TicketBookDto dto) {
        log.info("DTO:{}", dto);
        ticketService.bookTicket(dto);
        var responseEntity = ResponseEntity.created(URI.create("/my/tickets")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @PatchMapping("/{ticketId}/cancel")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> cancelTicket(@PathVariable Long ticketId) {
        log.info("Ticket id:{}", ticketId);
        ticketService.cancelTicket(ticketId);
        var responseEntity = ResponseEntity.ok().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<TicketDto> readAllTicketsByCurrentUser() {
        return ticketService.readByCurrentUser();
    }
}

