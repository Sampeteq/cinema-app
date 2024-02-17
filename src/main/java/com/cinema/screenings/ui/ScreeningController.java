package com.cinema.screenings.ui;

import com.cinema.screenings.application.ScreeningService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
class ScreeningController {

    private final ScreeningService screeningService;
    private final ScreeningMapper screeningMapper;

    @PostMapping("admin/screenings")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createScreening(@RequestBody @Valid ScreeningCreateRequest dto) {
        screeningService.createScreening(dto.date(), dto.filmId(), dto.hallId());
        return ResponseEntity.created(URI.create("/screenings")).build();
    }

    @DeleteMapping("/admin/screenings/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
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
