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

    private final FilmCreateService filmCreateService;
    private final FilmDeleteService filmDeleteService;
    private final FilmReadService filmReadService;
    private final ScreeningCreateService screeningCreateService;
    private final ScreeningDeleteService screeningDeleteService;
    private final ScreeningReadService screeningReadService;

    public void createFilm(FilmCreateDto dto) {
        filmCreateService.creteFilm(dto);
    }

    public void deleteFilm(String title) {
        filmDeleteService.delete(title);
    }

    public List<FilmDto> readAllFilms() {
        return filmReadService.readAll();
    }

    public void createScreening(ScreeningCreateDto dto) {
        screeningCreateService.createScreening(dto);
    }

    public void deleteScreening(Long id) {
        screeningDeleteService.delete(id);
    }

    public List<ScreeningDto> readAllScreeningsBy(ScreeningQueryDto queryDto) {
        return screeningReadService.readAllScreeningsBy(queryDto);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningReadService.readSeatsByScreeningId(id);
    }

    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        return screeningReadService.readScreeningDetails(id, rowNumber, seatNumber);
    }
}
