package code.screenings.client.commands.handlers;

import code.films.client.exceptions.FilmNotFoundException;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.rooms.client.exceptions.RoomNotFoundException;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.screenings.client.commands.CreateScreeningCommand;
import code.screenings.client.dto.ScreeningDto;
import code.screenings.client.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.Seat;
import code.screenings.domain.exceptions.WrongScreeningDateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Component
@AllArgsConstructor
public class CreateScreeningHandler {

    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningMapper screeningMapper;
    private final Clock clock;
    private final TransactionTemplate transactionTemplate;

    public ScreeningDto createScreening(CreateScreeningCommand dto) {
        validateScreeningDate(dto.date());
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

    private void validateScreeningDate(LocalDateTime date) {
        var currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC);
        var currentYear = currentDate.getYear();
        var isYearCurrentOrNextOne = date.getYear() == currentYear || date.getYear() == currentYear + 1;
        if (!isYearCurrentOrNextOne) {
            throw new WrongScreeningDateException();
        }
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
