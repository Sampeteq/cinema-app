package com.example.screening.domain;

import com.example.film.domain.FilmTestSpec;
import com.example.film.domain.dto.FilmDTO;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public class ScreeningTestSpec extends FilmTestSpec {

    public static final Year currentYear = Year.now();

    @Autowired
    public ScreeningAPI screeningAPI;

    public static AddScreeningDTO sampleAddScreeningDTO(Long filmId) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .freeSeats(100)
                .minAge(13)
                .build();
    }

    public static AddScreeningDTO sampleAddScreeningDTOwithWrongFilmYear(FilmDTO addedFilm) {
        return AddScreeningDTO
                .builder()
                .filmId(addedFilm.id())
                .date(LocalDateTime.of(currentYear.getValue() - 1, 1, 1, 18, 30))
                .freeSeats(100)
                .minAge(13)
                .build();
    }

    public ScreeningDTO addSampleScreening(Long sampleFilmId) {
        return screeningAPI.addScreening(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilmId)
                        .date(LocalDateTime.parse("2022-05-05T16:30"))
                        .freeSeats(100)
                        .minAge(13)
                        .build(),
                currentYear
        );
    }

    public ScreeningDTO addSampleScreening(Long sampleFilmId, LocalDateTime screeningDate) {
        return screeningAPI.addScreening(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilmId)
                        .date(screeningDate)
                        .freeSeats(100)
                        .minAge(13)
                        .build(),
                currentYear
        );
    }

    public ScreeningDTO addSampleScreeningWithNoFreeSeats(Long sampleFilmId) {
        return screeningAPI.addScreening(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilmId)
                        .date(LocalDateTime.parse("2022-05-05T16:30"))
                        .freeSeats(0)
                        .minAge(13)
                        .build(),
                currentYear
        );
    }

    public List<ScreeningDTO> addSampleScreenings(Long filmId) {
        var screening1 = screeningAPI.addScreening(
                AddScreeningDTO
                        .builder()
                        .filmId(filmId)
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .freeSeats(100)
                        .minAge(13)
                        .build(),
                currentYear
        );
        var screening2 = screeningAPI.addScreening(
                AddScreeningDTO
                        .builder()
                        .filmId(filmId)
                        .date(LocalDateTime.parse("2022-06-09T17:30"))
                        .freeSeats(100)
                        .minAge(13)
                        .build(),
                currentYear
        );
        return List.of(screening1, screening2);
    }
}
