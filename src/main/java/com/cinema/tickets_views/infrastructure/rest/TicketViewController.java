package com.cinema.tickets_views.infrastructure.rest;

import com.cinema.tickets_views.application.dto.TicketViewDto;
import com.cinema.tickets_views.application.services.TicketReadService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketViewController {

    private final TicketReadService ticketReadService;

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<TicketViewDto> readAllTickets() {
        return ticketReadService.readAll();
    }

    @GetMapping("/my/{ticketId}")
    @SecurityRequirement(name = "basic")
    TicketViewDto readTicketById(@PathVariable Long ticketId) {
        return ticketReadService.readById(ticketId);
    }
}
