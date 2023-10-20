package com.cinema.screenings.application.services;

import com.cinema.films.application.services.FilmService;
import com.cinema.screenings.application.dto.ScreeningCreateDto;
import com.cinema.screenings.application.dto.ScreeningDetailsDto;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.application.dto.ScreeningMapper;
import com.cinema.screenings.application.dto.ScreeningQueryDto;
import com.cinema.screenings.application.dto.SeatDetailsDto;
import com.cinema.screenings.application.dto.SeatDto;
import com.cinema.screenings.application.dto.SeatMapper;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.Seat;
import com.cinema.screenings.domain.SeatStatus;
import com.cinema.screenings.domain.events.ScreeningCreatedEvent;
import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.SeatNotFoundException;
import com.cinema.screenings.domain.policies.ScreeningDatePolicy;
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
    private final FilmService filmService;
    private final RoomService roomService;
    private final EventPublisher eventPublisher;

    public void createScreening(ScreeningCreateDto dto) {
        screeningDatePolicy.checkScreeningDate(dto.date());
        var filmDurationInMinutes = filmService.readFilmDurationInMinutes(dto.filmTitle());
        var endDate = dto.date().plusMinutes(filmDurationInMinutes);
        var roomDto = roomService.findFirstAvailableRoom(dto.date(), endDate);
        var seats = createSeats(roomDto.rowsNumber(), roomDto.rowSeatsNumber());
        var screening = new Screening(
                dto.date(),
                dto.filmTitle(),
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
                screening.getFilmTitle(),
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
