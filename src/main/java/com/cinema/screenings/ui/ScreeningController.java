package com.cinema.screenings.ui;

import com.cinema.screenings.application.ScreeningService;
import com.cinema.screenings.domain.Screening;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
class ScreeningController {

    private final ScreeningService screeningService;
    private final ScreeningMapper screeningMapper;

    @PostMapping("admin/screenings")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    Screening createScreening(@RequestBody @Valid ScreeningCreateRequest dto) {
        return screeningService.createScreening(dto.date(), dto.filmId(), dto.hallId());
    }

    @DeleteMapping("/admin/screenings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

    @GetMapping("/public/screenings")
    List<ScreeningView> getScreenings(@RequestParam(required = false) LocalDate date) {
        var screenings = date == null ?
                screeningService.getAllScreenings() :
                screeningService.getScreeningsByDate(date);
        return screenings
                .stream()
                .map(screeningMapper::mapScreeningToDto)
                .toList();
    }
}
