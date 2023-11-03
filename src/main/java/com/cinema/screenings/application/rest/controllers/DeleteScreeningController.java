package com.cinema.screenings.application.rest.controllers;

import com.cinema.screenings.application.commands.DeleteScreening;
import com.cinema.screenings.application.commands.handlers.DeleteScreeningHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
@Slf4j
class DeleteScreeningController {

    private final DeleteScreeningHandler deleteScreeningHandler;

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteScreening(@PathVariable Long id) {
        var command = new DeleteScreening(id);
        log.info("Command:{}", command);
        deleteScreeningHandler.handle(command);
        var responseEntity = ResponseEntity.noContent().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }
}
