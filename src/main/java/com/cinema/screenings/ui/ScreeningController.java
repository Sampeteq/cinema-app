package com.cinema.screenings.ui;

import com.cinema.screenings.application.ScreeningSeatService;
import com.cinema.screenings.application.ScreeningService;
import com.cinema.screenings.application.dto.CreateScreeningDto;
import com.cinema.screenings.application.dto.GetScreeningsDto;
import com.cinema.screenings.application.dto.ScreeningSeatDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class ScreeningController {

    private final ScreeningService screeningService;
    private final ScreeningSeatService screeningSeatService;

    @PostMapping("admin/screenings")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        log.info("Dto:{}", dto);
        screeningService.createScreening(dto);
        var responseEntity = ResponseEntity.created(URI.create("/screenings")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/admin/screenings/{id}")
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> deleteScreening(@PathVariable Long id) {
        log.info("Screening id:{}", id);
        screeningService.deleteScreening(id);
        var responseEntity = ResponseEntity.noContent().build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/public/screenings")
    ScreeningsResponse getScreenings(@RequestParam(required = false) LocalDate date) {
        var getScreeningsDto = GetScreeningsDto
                .builder()
                .date(date)
                .build();
        log.info("Dto:{}", getScreeningsDto);
        var screenings = screeningService.getScreenings(getScreeningsDto);
        return new ScreeningsResponse(screenings);
    }

    @GetMapping("/public/screenings/{id}/seats")
    List<ScreeningSeatDto> getSeatsByScreeningId(@PathVariable Long id) {
        log.info("Screening id:{}", id);
        return screeningSeatService.getSeatsByScreeningId(id);
    }
}
