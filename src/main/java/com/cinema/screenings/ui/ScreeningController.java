package com.cinema.screenings.ui;

import com.cinema.screenings.application.ScreeningService;
import com.cinema.screenings.application.dto.ScreeningSeatDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping("admin/screenings")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createScreening(@RequestBody @Valid CreateScreeningDto dto) {
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
    ScreeningsResponse getScreenings(@RequestParam(required = false) LocalDate date) {
        var screenings = date == null ? screeningService.getAllScreenings() : screeningService.getScreeningsByDate(date);
        return new ScreeningsResponse(screenings);
    }

    @GetMapping("/public/screenings/{id}/seats")
    List<ScreeningSeatDto> getSeatsByScreeningId(@PathVariable Long id) {
        return screeningService.getSeatsByScreeningId(id);
    }
}
