package com.cinema.tickets.application.rest.controllers;

import com.cinema.tickets.application.queries.ReadAllTicketsByCurrentUser;
import com.cinema.tickets.application.queries.dto.TicketDto;
import com.cinema.tickets.application.queries.handlers.ReadAllTicketsByCurrentUserHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@Tag(name = "tickets")
@RequiredArgsConstructor
@Slf4j
class ReadTicketController {

    private final ReadAllTicketsByCurrentUserHandler readAllTicketsByCurrentUserHandler;

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<TicketDto> readAllTicketsByCurrentUser() {
        var query = new ReadAllTicketsByCurrentUser();
        return readAllTicketsByCurrentUserHandler.handle(query);
    }
}
