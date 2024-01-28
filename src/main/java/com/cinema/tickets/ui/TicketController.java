package com.cinema.tickets.ui;

import com.cinema.tickets.application.TicketService;
import com.cinema.tickets.application.dto.BookTicketDto;
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

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> bookTicket(@RequestBody @Valid BookTicketDto dto) {
        log.info("Dto:{}", dto);
        ticketService.bookTicket(dto);
        var responseEntity = ResponseEntity.ok().build();
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
    TicketsResponse getAllTicketsByLoggedUser() {
        var tickets = ticketService.getAllTicketsByLoggedUser();
        return new TicketsResponse(tickets);
    }
}
