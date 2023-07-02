package code.catalog.application.services;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Screening;
import code.catalog.domain.Seat;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.services.ScreeningDateValidateService;
import code.catalog.infrastructure.db.FilmRepository;
import code.catalog.infrastructure.db.RoomRepository;
import code.shared.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Service
@RequiredArgsConstructor
public class ScreeningCreateService {

    private final ScreeningDateValidateService screeningDateValidator;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;

    public Long createScreening(ScreeningCreateDto dto) {
        screeningDateValidator.validate(dto.date(), clock);
        var screening = transactionTemplate.execute(status -> {
            var film = getFilmOrThrow(dto.filmId());
            var screeningDate = dto.date();
            var screeningFinishDate = dto.date().plusMinutes(film.getDurationInMinutes());
            var availableRoom = getFirstAvailableRoom(
                    screeningDate,
                    screeningFinishDate
            );
            var seats = createSeats(availableRoom);
            var newScreening = Screening.create(dto.date(), film, availableRoom, seats);
            film.addScreening(newScreening);
            return newScreening;
        });
        return screening.getId();
    }

    private Film getFilmOrThrow(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private Room getFirstAvailableRoom(LocalDateTime start, LocalDateTime finish) {
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room
                        .getScreenings()
                        .stream()
                        .noneMatch(screening -> screening.isCollisionWith(start, finish))
                )
                .findFirst()
                .orElseThrow(RoomsNoAvailableException::new);
    }

    private static List<Seat> createSeats(Room room) {
        return rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber))
                ).toList();
    }
}
