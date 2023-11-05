package com.cinema.screenings.application.rest.controllers;

import com.cinema.screenings.application.queries.ReadSeatsByScreeningId;
import com.cinema.screenings.application.queries.dto.SeatDto;
import com.cinema.screenings.application.queries.handlers.ReadSeatsByScreeningIdHandler;
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
public class ReadSeatsController {

    private final ReadSeatsByScreeningIdHandler readSeatsByScreeningIdHandler;

    @GetMapping("/{id}/seats")
    List<SeatDto> readSeatsByScreeningId(@PathVariable Long id) {
        var query = new ReadSeatsByScreeningId(id);
        log.info("Query:{}", query);
        return readSeatsByScreeningIdHandler.handle(query);
    }
}
