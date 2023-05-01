package code.screenings.client.commands.handlers;

import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.screenings.client.dto.ScreeningDto;
import code.screenings.client.dto.ScreeningMapper;
import code.screenings.client.commands.CreateScreeningCommand;
import code.screenings.domain.Screening;
import code.screenings.domain.exceptions.WrongScreeningDateException;
import code.films.client.exceptions.FilmNotFoundException;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.client.exceptions.RoomNotFoundException;
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
    private final ScreeningMapper screeningMapper;
    private final Clock clock;

    @Transactional
    public ScreeningDto createScreening(CreateScreeningCommand dto) {
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
        return screeningMapper.mapToDto(newScreening);
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
