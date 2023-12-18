package com.cinema.screenings.ui;

import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.screenings.application.commands.DeleteScreening;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import com.cinema.screenings.application.commands.handlers.DeleteScreeningHandler;
import com.cinema.screenings.application.queries.GetScreenings;
import com.cinema.screenings.application.queries.GetSeatsByScreeningId;
import com.cinema.screenings.application.queries.dto.ScreeningSeatDto;
import com.cinema.screenings.application.queries.handlers.GetScreeningsHandler;
import com.cinema.screenings.application.queries.handlers.GetSeatsByScreeningIdHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
class ScreeningController {

    private final CreateScreeningHandler createScreeningHandler;
    private final DeleteScreeningHandler deleteScreeningHandler;
    private final GetScreeningsHandler getScreeningsHandler;
    private final GetSeatsByScreeningIdHandler getSeatsByScreeningIdHandler;

    @PostMapping("admin/screenings")
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

    @DeleteMapping("/admin/screenings/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteScreening(@PathVariable Long id) {
        var command = new DeleteScreening(id);
        log.info("Command:{}", command);
        deleteScreeningHandler.handle(command);
        var responseEntity = ResponseEntity.noContent().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/public/screenings")
    ScreeningsResponse getScreenings(@RequestParam(required = false) LocalDate date) {
        var query = GetScreenings
                .builder()
                .date(date)
                .build();
        log.info("Query:{}", query);
        var screenings = getScreeningsHandler.handle(query);
        return new ScreeningsResponse(screenings);
    }

    @GetMapping("/public/screenings/{id}/seats")
    List<ScreeningSeatDto> getSeatsByScreeningId(@PathVariable Long id) {
        var query = new GetSeatsByScreeningId(id);
        log.info("Query:{}", query);
        return getSeatsByScreeningIdHandler.handle(query);
    }
}
