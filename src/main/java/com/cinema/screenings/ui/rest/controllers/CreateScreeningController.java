package com.cinema.screenings.ui.rest.controllers;

import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
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
@RequestMapping("/screenings")
@Tag(name = "screenings")
@RequiredArgsConstructor
@Slf4j
class CreateScreeningController {

    private final CreateScreeningHandler createScreeningHandler;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createScreening(
            @RequestBody
            @Valid
            CreateScreening command
    ) {
        log.info("Command:{}", command);
        createScreeningHandler.handle(command);
        var responseEntity = ResponseEntity.created(URI.create("/screenings")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }
}
