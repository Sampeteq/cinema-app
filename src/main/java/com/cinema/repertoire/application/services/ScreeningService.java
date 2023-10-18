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
import com.cinema.repertoire.domain.exceptions.FilmNotFoundException;
import com.cinema.repertoire.domain.exceptions.ScreeningNotFoundException;
import com.cinema.repertoire.domain.exceptions.SeatNotFoundException;
import com.cinema.repertoire.domain.policies.ScreeningDatePolicy;
import com.cinema.rooms.application.services.RoomService;
import com.cinema.shared.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningDatePolicy screeningDatePolicy;
    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final SeatMapper seatMapper;
    private final FilmRepository filmRepository;
    private final RoomService roomService;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        screeningDatePolicy.checkScreeningDate(dto.date());
        var film = filmRepository
                .readByTitle(dto.filmTitle())
                .orElseThrow(FilmNotFoundException::new);
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
                .orElseThrow(ScreeningNotFoundException::new)
                .getDate();
    }

    public ScreeningDetailsDto readScreeningDetails(Long screeningId) {
        var screening = screeningRepository
                .readById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
        return new ScreeningDetailsDto(
                screening.getDate(),
                screening.getFilm().getTitle(),
                screening.getRoomId()
        );
    }

    public SeatDetailsDto readSeatDetails(Long screeningId, Long seatId) {
        var seat = screeningRepository
                .readById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .findSeat(seatId)
                .orElseThrow(SeatNotFoundException::new);
        return new SeatDetailsDto(
                seat.getRowNumber(),
                seat.getNumber()
        );
    }

    public boolean seatExists(Long screeningId, Long seatId) {
        return screeningRepository
                .readById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new)
                .hasSeat(seatId);
    }

    public List<SeatDto> readSeatsByScreeningId(Long id) {
        return screeningRepository
                .readById(id)
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeats()
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }

    public void delete(Long id) {
        var screening = screeningRepository
                .readById(id)
                .orElseThrow(ScreeningNotFoundException::new);
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
