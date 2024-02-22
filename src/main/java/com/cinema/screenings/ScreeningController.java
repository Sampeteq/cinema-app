package com.cinema.screenings;

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

    @PostMapping("admin/screenings")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    Screening createScreening(@RequestBody @Valid Screening screening) {
        return screeningService.addScreening(screening);
    }

    @DeleteMapping("/admin/screenings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

    @GetMapping("/public/screenings")
    List<Screening> getScreenings(@RequestParam(required = false) LocalDate date) {
        return date == null ?
                screeningService.getAllScreenings() :
                screeningService.getScreeningsByDate(date);
    }
}
