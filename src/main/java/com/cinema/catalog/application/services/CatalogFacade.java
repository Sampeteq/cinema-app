package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.application.dto.FilmDto;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.application.dto.ScreeningDetailsDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogFacade {

    private final FilmCreateService filmCreateService;
    private final FilmReadService filmReadService;
    private final ScreeningCreateService screeningCreateService;
    private final ScreeningReadService screeningReadService;
    private final SeatReadService seatReadService;

    public void createFilm(FilmCreateDto dto) {
        filmCreateService.creteFilm(dto);
    }

    public List<FilmDto> readAllFilms() {
        return filmReadService.readAll();
    }

    public void createScreening(ScreeningCreateDto dto) {
        screeningCreateService.createScreening(dto);
    }

    public List<ScreeningDto> readAllScreenings() {
        return screeningReadService.readAllScreenings();
    }

    public List<ScreeningDto> readScreeningsByFilmTitle(String title) {
        return screeningReadService.readScreeningsByFilmTitle(title);
    }

    public List<ScreeningDto> readScreeningsByFilmCategory(FilmCategory category) {
        return screeningReadService.readScreeningsByFilmCategory(category);
    }

    public List<ScreeningDto> readScreeningsByDate(LocalDate date) {
        return screeningReadService.readScreeningsByDate(date);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return seatReadService.readSeatsByScreeningId(id);
    }

    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        var screening = screeningReadService.readByIdWithSeats(id);
        var seat = screening.findSeat(rowNumber, seatNumber);
        var seatExists = seat.isPresent();
        return new ScreeningDetailsDto(
                screening.getFilm().getTitle(),
                screening.getDate(),
                screening.getRoomCustomId(),
                seatExists
        );
    }
}
