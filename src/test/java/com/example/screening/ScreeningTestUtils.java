package com.example.screening;

import com.example.screening.dto.AddScreeningDTO;
import com.example.screening.dto.ScreeningDTO;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public class ScreeningTestUtils {

    public static final Year currentYear = Year.now();

    public static AddScreeningDTO sampleAddScreeningDTO(Long filmId) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .freeSeats(100)
                .minAge(13)
                .build();
    }

    public static ScreeningDTO addSampleScreening(Long filmId, ScreeningFacade screeningFacade) {
        var dto = AddScreeningDTO
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .freeSeats(100)
                .minAge(13)
                .build();
        return screeningFacade.add(dto, currentYear);
    }

    public static ScreeningDTO addSampleScreeningWithWrongFilmYear(Long filmId, ScreeningFacade screeningFacade) {
        var dto = AddScreeningDTO
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.of(currentYear.getValue() - 1, 1, 1, 18, 30))
                .freeSeats(100)
                .minAge(13)
                .build();
        return screeningFacade.add(dto, currentYear);
    }

    public static ScreeningDTO addSampleScreeningWithNoFreeSeats(Long sampleFilmId, ScreeningFacade screeningFacade) {
        return screeningFacade.add(
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

    public static List<ScreeningDTO> addSampleDistinctScreenings(Long filmId, ScreeningFacade screeningFacade) {
        var screening1 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(filmId)
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .freeSeats(100)
                        .minAge(13)
                        .build(),
                currentYear
        );
        var screening2 = screeningFacade.add(
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
