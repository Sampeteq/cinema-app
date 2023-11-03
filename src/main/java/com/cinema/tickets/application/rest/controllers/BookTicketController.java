package com.cinema.tickets.application.rest.controllers;

import com.cinema.tickets.application.commands.BookTicket;
import com.cinema.tickets.application.commands.handlers.BookTicketHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/tickets")
@Tag(name = "tickets")
@RequiredArgsConstructor
@Slf4j
class BookTicketController {

    private final BookTicketHandler bookTicketHandler;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> bookTicket(@RequestBody @Valid BookTicket command) {
        log.info("Command:{}", command);
        bookTicketHandler.handle(command);
        var responseEntity = ResponseEntity.created(URI.create("/my/tickets")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }
}
