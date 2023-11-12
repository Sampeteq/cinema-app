package com.cinema.screenings.ui.rest.controllers;

import com.cinema.screenings.application.queries.GetScreeningsBy;
import com.cinema.screenings.application.queries.dto.ScreeningDto;
import com.cinema.screenings.application.queries.handlers.GetScreeningsByHandler;
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
class GetScreeningController {

    private final GetScreeningsByHandler getScreeningsByHandler;

    @GetMapping
    List<ScreeningDto> readScreeningsBy(@RequestParam(required = false) LocalDate date) {
        var query = GetScreeningsBy
                .builder()
                .date(date)
                .build();
        log.info("Query:{}", query);
        return getScreeningsByHandler.handle(query);
    }
}
