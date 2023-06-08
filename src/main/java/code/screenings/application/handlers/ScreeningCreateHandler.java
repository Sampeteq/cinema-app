package code.screenings.application.handlers;

import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.screenings.application.commands.ScreeningCreateCommand;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningDateValidator;
import code.screenings.domain.Seat;
import code.screenings.domain.exceptions.RoomsNoAvailableException;
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
public class ScreeningCreateHandler {

    private final ScreeningDateValidator screeningDateValidator;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningMapper screeningMapper;

    public ScreeningDto handle(ScreeningCreateCommand command) {
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
            availableRoom.addScreening(newScreening);
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
