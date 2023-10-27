package com.cinema.screenings.application.rest;

import com.cinema.screenings.application.dto.ScreeningCreateDto;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningQueryDto;
import com.cinema.screenings.application.dto.SeatDto;
import com.cinema.screenings.application.services.ScreeningService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
@Slf4j
class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    @SecurityRequirement(name = "basic")
    ResponseEntity<Object> createScreening(
            @RequestBody
            @Valid
            ScreeningCreateDto dto
    ) {
        log.info("DTO:{}", dto);
        screeningService.createScreening(dto);
        var responseEntity = ResponseEntity.created(URI.create("/screenings")).build();
        log.info("Response entity:{}", responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteScreening(@PathVariable Long id) {
        screeningService.delete(id);
    }

    @GetMapping
    List<ScreeningDto> readAllScreeningsBy(@RequestParam(required = false) LocalDate date) {
        var queryDto = ScreeningQueryDto
                .builder()
                .date(date)
                .build();
        return screeningService.readAllScreeningsBy(queryDto);
    }

    @GetMapping("/{id}/seats")
    List<SeatDto> readSeatsByScreeningId(@PathVariable Long id) {
        return screeningService.readSeatsByScreeningId(id);
    }
}
