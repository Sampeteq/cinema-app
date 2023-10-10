package com.cinema.repertoire.application.rest;

import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.application.dto.ScreeningDto;
import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.application.dto.SeatDto;
import com.cinema.repertoire.application.services.ScreeningService;
import com.cinema.repertoire.domain.FilmCategory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    void createScreening(
            @RequestBody
            @Valid
            ScreeningCreateDto dto
    ) {
        screeningService.createScreening(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        screeningService.delete(id);
    }

    @GetMapping
    List<ScreeningDto> readAllScreeningsBy(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String filmTitle,
            @RequestParam(required = false) FilmCategory filmCategory
    ) {
        var queryDto = ScreeningQueryDto
                .builder()
                .date(date)
                .filmTitle(filmTitle)
                .filmCategory(filmCategory)
                .build();
        return screeningService.readAllScreeningsBy(queryDto);
    }

    @GetMapping("/{id}/seats")
    List<SeatDto> readSeatsByScreeningId(@PathVariable Long id) {
        return screeningService.readSeatsByScreeningId(id);
    }
}
