package com.cinema.catalog.infrastructure.rest;

import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.catalog.domain.FilmCategory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    private final CatalogFacade catalogFacade;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    void createScreening(
            @RequestBody
            @Valid
            ScreeningCreateDto dto
    ) {
        catalogFacade.createScreening(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        catalogFacade.deleteScreening(id);
    }

    @GetMapping
    List<ScreeningDto> readAllScreenings() {
        return catalogFacade.readAllScreenings();
    }

    @GetMapping("/by/title")
    List<ScreeningDto> readScreeningsByFilmTitle(@RequestParam String title) {
        return catalogFacade.readScreeningsByFilmTitle(title);
    }

    @GetMapping("/by/category")
    List<ScreeningDto> readScreeningsByFilmCategory(@RequestParam FilmCategory category) {
        return catalogFacade.readScreeningsByFilmCategory(category);
    }

    @GetMapping("/by/date")
    List<ScreeningDto> readByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return catalogFacade.readScreeningsByDate(date);
    }

    @GetMapping("/{id}/seats")
    List<SeatDto> readSeatsByScreeningId(@PathVariable Long id) {
        return catalogFacade.readSeatsByScreeningId(id);
    }
}
