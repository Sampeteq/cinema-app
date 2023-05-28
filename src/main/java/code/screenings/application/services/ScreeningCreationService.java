package code.screenings.application.services;

import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.screenings.application.commands.ScreeningCreationCommand;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningDateValidator;
import code.screenings.domain.Seat;
import code.screenings.domain.exceptions.NoAvailableRoomsException;
import code.shared.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Component
@AllArgsConstructor
public class ScreeningCreationService {

    private final ScreeningDateValidator screeningDateValidator;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningMapper screeningMapper;

    public ScreeningDto createScreening(ScreeningCreationCommand command) {
        screeningDateValidator.validate(command.date(), clock);
        var screening = transactionTemplate.execute(status -> {
            var film = getFilmOrThrow(command.filmId());
            var newScreening = Screening.of(
                    command.date(),
                    film
            );
            var availableRoom = getFirstAvailableRoom(newScreening);
            var seats = createSeats(availableRoom, newScreening);
            newScreening.addSeats(seats);
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

    private Room getFirstAvailableRoom(Screening newScreening) {
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room
                        .getScreenings()
                        .stream()
                        .noneMatch(screening -> screening.isCollisionWith(newScreening))
                )
                .findFirst()
                .orElseThrow(NoAvailableRoomsException::new);
    }

    private static List<Seat> createSeats(Room room, Screening screening) {
        return rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber, screening))
                ).toList();
    }
}
