package code.catalog.application.services;

import code.catalog.application.dto.ScreeningMapper;
import code.catalog.domain.Film;
import code.catalog.domain.Room;
import code.catalog.domain.Seat;
import code.catalog.infrastructure.db.FilmRepository;
import code.catalog.domain.Screening;
import code.catalog.domain.ScreeningDateValidator;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.infrastructure.db.RoomRepository;
import code.catalog.application.commands.ScreeningCreateCommand;
import code.catalog.application.dto.ScreeningDto;
import code.shared.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Component
@AllArgsConstructor
public class ScreeningCreateService {

    private final ScreeningDateValidator screeningDateValidator;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningMapper screeningMapper;

    public ScreeningDto createScreening(ScreeningCreateCommand command) {
        screeningDateValidator.validate(command.date(), clock);
        var screening = transactionTemplate.execute(status -> {
            var film = getFilmOrThrow(command.filmId());
            var screeningDate = command.date();
            var screeningFinishDate = command.date().plusMinutes(film.getDurationInMinutes());
            var availableRoom = getFirstAvailableRoom(
                    screeningDate,
                    screeningFinishDate
            );
            var seats = createSeats(availableRoom);
            var newScreening = Screening.create(command.date(), film, availableRoom, seats);
            film.addScreening(newScreening);
            return newScreening;
        });
        return screeningMapper.mapToDto(screening);
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
