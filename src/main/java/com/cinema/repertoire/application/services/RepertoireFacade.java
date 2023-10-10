package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.FilmCreateDto;
import com.cinema.repertoire.application.dto.FilmDto;
import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.application.dto.ScreeningDetailsDto;
import com.cinema.repertoire.application.dto.ScreeningDto;
import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RepertoireFacade {

    private final FilmService filmService;
    private final ScreeningService screeningService;

    public void createFilm(FilmCreateDto dto) {
        filmService.creteFilm(dto);
    }

    public void deleteFilm(String title) {
        filmService.delete(title);
    }

    public List<FilmDto> readAllFilms() {
        return filmService.readAll();
    }

    public void createScreening(ScreeningCreateDto dto) {
        screeningService.createScreening(dto);
    }

    public void deleteScreening(Long id) {
        screeningService.delete(id);
    }

    public List<ScreeningDto> readAllScreeningsBy(ScreeningQueryDto queryDto) {
        return screeningService.readAllScreeningsBy(queryDto);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningService.readSeatsByScreeningId(id);
    }

    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        return screeningService.readScreeningDetails(id, rowNumber, seatNumber);
    }
}
