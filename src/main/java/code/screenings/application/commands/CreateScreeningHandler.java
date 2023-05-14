package code.screenings.application.commands;

import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.films.domain.exceptions.FilmNotFoundException;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.exceptions.RoomNotFoundException;
import code.screenings.application.commands.CreateScreeningCommand;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningDateValidator;
import code.screenings.domain.Seat;
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

    public ScreeningDto createScreening(CreateScreeningCommand dto) {
        screeningDateValidator.validate(dto.date(), clock);
        var screening = transactionTemplate.execute(status ->{
            var film = getFilmOrThrow(dto);
            var room = getRoomOrThrow(dto.roomId());
            var newScreening = Screening.of(
                    dto.date(),
                    film,
                    room
            );
            var seats = createSeats(room, newScreening);
            newScreening.addSeats(seats);
            film.addScreening(newScreening);
            return newScreening;
        });
        return screeningMapper.mapToDto(screening);
    }

    private Film getFilmOrThrow(CreateScreeningCommand dto) {
        return filmRepository
                .readById(dto.filmId())
                .orElseThrow(FilmNotFoundException::new);
    }

    private Room getRoomOrThrow(Long roomId) {
        return roomRepository
                .readById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

    private static List<Seat> createSeats(Room room, Screening screening) {
        return rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber, screening))
                ).toList();
    }
}
