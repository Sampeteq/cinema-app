package com.cinema.screenings.application.rest;

import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.application.commands.DeleteScreening;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.SeatDto;
import com.cinema.screenings.application.handlers.CreateScreeningHandler;
import com.cinema.screenings.application.handlers.DeleteScreeningHandler;
import com.cinema.screenings.application.handlers.ReadScreeningsByHandler;
import com.cinema.screenings.application.handlers.ReadSeatsByScreeningIdHandler;
import com.cinema.screenings.application.queries.ReadScreeningsBy;
import com.cinema.screenings.application.queries.ReadSeatsByScreeningId;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
@Slf4j
class ScreeningController {

    private final CreateScreeningHandler createScreeningHandler;
    private final DeleteScreeningHandler deleteScreeningHandler;
    private final ReadScreeningsByHandler readScreeningsByHandler;
    private final ReadSeatsByScreeningIdHandler readSeatsByScreeningIdHandler;

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

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        var command = new DeleteScreening(id);
        deleteScreeningHandler.handle(command);
    }

    @GetMapping
    List<ScreeningDto> readScreeningsBy(@RequestParam(required = false) LocalDate date) {
        var query = ReadScreeningsBy
                .builder()
                .date(date)
                .build();
        log.info("Query:{}", query);
        return readScreeningsByHandler.handle(query);
    }

    @GetMapping("/{id}/seats")
    List<SeatDto> readSeatsByScreeningId(@PathVariable Long id) {
        var query = new ReadSeatsByScreeningId(id);
        log.info("Query:{}", query);
        return readSeatsByScreeningIdHandler.handle(query);
    }
}
