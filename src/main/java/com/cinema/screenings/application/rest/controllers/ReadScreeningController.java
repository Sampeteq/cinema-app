package com.cinema.screenings.application.rest.controllers;

import com.cinema.screenings.application.queries.ReadScreeningsBy;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.handlers.ReadScreeningsByHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/screenings")
@Tag(name = "screenings")
@RequiredArgsConstructor
@Slf4j
class ReadScreeningController {

    private final ReadScreeningsByHandler readScreeningsByHandler;

    @GetMapping
    List<ScreeningDto> readScreeningsBy(@RequestParam(required = false) LocalDate date) {
        var query = ReadScreeningsBy
                .builder()
                .date(date)
                .build();
        log.info("Query:{}", query);
        return readScreeningsByHandler.handle(query);
    }
}
