package com.cinema.repertoire.application.services;

import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.application.dto.ScreeningDetailsDto;
import com.cinema.repertoire.application.dto.ScreeningDto;
import com.cinema.repertoire.application.dto.ScreeningMapper;
import com.cinema.repertoire.application.dto.ScreeningQueryDto;
import com.cinema.repertoire.application.dto.SeatDetailsDto;
import com.cinema.repertoire.application.dto.SeatDto;
import com.cinema.repertoire.application.dto.SeatMapper;
import com.cinema.repertoire.domain.FilmRepository;
import com.cinema.repertoire.domain.Screening;
import com.cinema.repertoire.domain.ScreeningRepository;
import com.cinema.repertoire.domain.Seat;
import com.cinema.repertoire.domain.SeatStatus;
import com.cinema.repertoire.domain.events.ScreeningCreatedEvent;
import com.cinema.repertoire.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomService;
import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final RoomService roomService;
    private final Clock clock;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        var daysDifference = Duration
                .between(LocalDateTime.now(clock), dto.date())
                .abs()
                .toDays();
        var isScreeningDateOutOfRange = daysDifference < 7 || daysDifference > 21;
        if (isScreeningDateOutOfRange) {
            throw new ScreeningDateOutOfRangeException();
        }
        var film = filmRepository
                .readByTitle(dto.filmTitle())
                .orElseThrow(() -> new EntityNotFoundException("Film"));
        var endDate = film.calculateScreeningEndDate(dto.date());
        var roomDto = roomService.findFirstAvailableRoom(dto.date(), endDate);
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

    public List<ScreeningDto> readAllScreeningsBy(ScreeningQueryDto queryDto) {
        return screeningRepository
                .readAllBy(queryDto)
                .stream()
                .sorted(comparing(Screening::getDate))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public LocalDateTime readScreeningDate(Long screeningId) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .getDate();
    }

    public ScreeningDetailsDto readScreeningDetails(Long screeningId) {
        var screening = screeningRepository
                .readById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        return new ScreeningDetailsDto(
                screening.getDate(),
                screening.getFilm().getTitle(),
                screening.getRoomId()
        );
    }

    public SeatDetailsDto readSeatDetails(Long screeningId, Long seatId) {
        var seat = screeningRepository
                .readById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .findSeat(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        return new SeatDetailsDto(
                seat.getRowNumber(),
                seat.getNumber()
        );
    }

    public boolean seatExists(Long screeningId, Long seatId) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .hasSeat(seatId);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"))
                .getSeats()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        screeningRepository.delete(screening);
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
