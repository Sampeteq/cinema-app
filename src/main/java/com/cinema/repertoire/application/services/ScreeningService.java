package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.application.dto.ScreeningDetailsDto;
import com.cinema.repertoire.application.dto.ScreeningDto;
import com.cinema.repertoire.application.dto.ScreeningMapper;
import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.application.dto.SeatDto;
import com.cinema.repertoire.application.dto.SeatMapper;
import com.cinema.repertoire.domain.Film;
import com.cinema.repertoire.domain.FilmRepository;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.Seat;
import com.cinema.repertoire.domain.SeatStatus;
import com.cinema.repertoire.domain.events.ScreeningCreatedEvent;
import com.cinema.repertoire.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final SeatMapper seatMapper;
    private final FilmRepository filmRepository;
    private final RoomFacade roomFacade;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        if (isScreeningDateOutOfRange(dto.date())) {
            throw new ScreeningDateOutOfRangeException();
        }
        var film = readFilm(dto.filmTitle());
        var endDate = film.calculateScreeningEndDate(dto.date());
        var roomDto = roomFacade.findFirstAvailableRoom(dto.date(), endDate);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.rowSeatsNumber());
        var screening = new Screening(
                dto.date(),
                film,
                roomDto.id(),
                seats
        );
        screeningRepository.add(screening);
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                screening.getDate(),
                endDate,
                screening.getRoomId()
        );
        eventPublisher.publish(screeningCreatedEvent);
    }

    @Transactional(readOnly = true)
    public List<ScreeningDto> readAllScreeningsBy(ScreeningQueryDto queryDto) {
        return screeningRepository
                .readAllBy(queryDto)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ScreeningDetailsDto readScreeningDetails(Long id, int rowNumber, int seatNumber) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        var seat = screening.findSeat(rowNumber, seatNumber);
        var seatExists = seat.isPresent();
        return new ScreeningDetailsDto(
                screening.getFilm().getTitle(),
                screening.getDate(),
                screening.getRoomId(),
                seatExists
        );
    }

    @Transactional(readOnly = true)
    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .getSeats()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }

    void delete(Long id) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        screeningRepository.delete(screening);
    }

    private Film readFilm(String filmTitle) {
        return filmRepository
                .readByTitle(filmTitle)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private boolean isScreeningDateOutOfRange(LocalDateTime screeningDate) {
        var currentDate = LocalDateTime.now(clock);
        var datesDifference = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toDays();
        return datesDifference < 7 || datesDifference > 21;
    }

    private List<Seat> createSeats(int rowsQuantity, int seatsQuantityInOneRow) {
        return IntStream
                .rangeClosed(1, rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatsQuantityInOneRow)
                        .mapToObj(seatNumber -> new Seat(rowNumber, seatNumber, SeatStatus.FREE))
                )
                .toList();
    }
}
