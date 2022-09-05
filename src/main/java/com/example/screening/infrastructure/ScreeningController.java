package com.example.screening.infrastructure;

import com.example.screening.domain.ScreeningAPI;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ScreeningController {

    private final ScreeningAPI screeningAPI;

    @PostMapping("/screenings")
    ScreeningDTO addNewScreening(@RequestBody @Valid AddScreeningDTO dto) {
        return screeningAPI.addScreening(dto);
    }

    @GetMapping("/screenings")
    List<ScreeningDTO> readAllScreenings() {
        return screeningAPI.readAllScreenings();
    }

    @GetMapping("/screenings/film/{filmId}")
    List<ScreeningDTO> readScreeningsByFilmId(@PathVariable UUID filmId) {
        return screeningAPI.readScreeningsByFilmId(filmId);
    }

    @GetMapping("/screenings/date")
    List<ScreeningDTO> readScreeningsByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        return screeningAPI.readAllScreeningsByDate(date);
    }
}
