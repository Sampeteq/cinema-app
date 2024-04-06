package com.cinema.screenings.infrastructure.ui;

import com.cinema.screenings.domain.ScreeningCreateDto;
import com.cinema.screenings.domain.ScreeningService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    ScreeningDto createScreening(@RequestBody @Valid ScreeningCreateDto screeningCreateDto) {
        return screeningMapper.mapToDto(screeningService.createScreening(screeningCreateDto));
    }

    @DeleteMapping("/admin/screenings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

    @GetMapping("/public/screenings")
    List<ScreeningDto> getScreenings(@RequestParam(required = false) LocalDate date) {
        return (date == null ?
                screeningService.getAllScreenings() :
                screeningService.getScreeningsByDate(date))
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
