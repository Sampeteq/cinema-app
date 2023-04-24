package code.films.client.commands.handlers;

import code.films.client.commands.CreateScreeningCommand;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.films.domain.Screening;
import code.films.domain.exceptions.WrongScreeningDateException;
import code.films.infrastructure.exceptions.FilmNotFoundException;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.infrastructure.exceptions.RoomNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CreateScreeningHandler {

    private final FilmRepository filmRepository;

    private final RoomRepository roomRepository;

    private final Clock clock;

    @Transactional
    public Screening createScreening(CreateScreeningCommand dto) {
        validateScreeningDate(dto.date());
        var film = getFilmOrThrow(dto);
        var room = getRoomOrThrow(dto.roomId());
        var newScreening = Screening.of(
                dto.date(),
                dto.minAge(),
                film,
                room
        );
        film.addScreening(newScreening);
        return newScreening;
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

    private Room getRoomOrThrow(UUID roomId) {
        return roomRepository
                .readById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }
}