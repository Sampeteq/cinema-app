package com.cinema.tickets.ui.rest.controllers;

import com.cinema.tickets.application.queries.GetSeatsByScreeningId;
import com.cinema.tickets.application.queries.dto.SeatDto;
import com.cinema.tickets.application.queries.handlers.GetSeatsByScreeningIdHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/screenings")
@Tag(name = "screenings")
@RequiredArgsConstructor
@Slf4j
public class GetSeatsController {

    private final GetSeatsByScreeningIdHandler getSeatsByScreeningIdHandler;

    @GetMapping("/{id}/seats")
    List<SeatDto> getSeatsByScreeningId(@PathVariable Long id) {
        var query = new GetSeatsByScreeningId(id);
        log.info("Query:{}", query);
        return getSeatsByScreeningIdHandler.handle(query);
    }
}
