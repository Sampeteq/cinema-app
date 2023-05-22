package code.screenings.application.commands;

import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningDateValidator;
import code.screenings.domain.Seat;
import code.shared.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Component
@AllArgsConstructor
public class CreateScreeningHandler {

    private final ScreeningDateValidator screeningDateValidator;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningMapper screeningMapper;

    public ScreeningDto handle(CreateScreeningCommand command) {
        screeningDateValidator.validate(command.date(), clock);
        var screening = transactionTemplate.execute(status -> createScreening(command));
        return screeningMapper.mapToDto(screening);
    }

    private Screening createScreening(CreateScreeningCommand command) {
        var film = getFilmOrThrow(command.filmId());
        var room = getRoomOrThrow(command.roomId());
        var newScreening = Screening.of(
                command.date(),
                film,
                room
        );
        var seats = createSeats(room, newScreening);
        newScreening.addSeats(seats);
        room.addScreening(newScreening);
        return newScreening;
    }

    private Film getFilmOrThrow(Long filmId) {
        return filmRepository
                .readById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film"));
    }

    private Room getRoomOrThrow(Long roomId) {
        return roomRepository
                .readById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room"));
    }

    private static List<Seat> createSeats(Room room, Screening screening) {
        return rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber, screening))
                ).toList();
    }
}
