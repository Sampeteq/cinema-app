package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.application.dto.FilmDto;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.application.dto.ScreeningDetailsDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.ScreeningQueryDto;
import com.cinema.catalog.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogFacade {

    private final FilmCreateService filmCreateService;
    private final FilmDeleteService filmDeleteService;
    private final FilmReadService filmReadService;
    private final ScreeningCreateService screeningCreateService;
    private final ScreeningDeleteService screeningDeleteService;
    private final ScreeningReadService screeningReadService;
    private final SeatReadService seatReadService;

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

    public List<ScreeningDto> readAllBy(ScreeningQueryDto queryDto) {
        return screeningReadService.readAllScreenings(queryDto);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return seatReadService.readSeatsByScreeningId(id);
    }

    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        var screening = screeningReadService.readById(id);
        var seat = screening.findSeat(rowNumber, seatNumber);
        var seatExists = seat.isPresent();
        return new ScreeningDetailsDto(
                screening.getFilm().getTitle(),
                screening.getDate(),
                screening.getRoomId(),
                seatExists
        );
    }
}
