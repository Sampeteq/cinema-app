package code.bookings.application.services;

import code.bookings.application.dto.CreateScreeningDto;
import code.bookings.domain.Film;
import code.bookings.domain.FilmRepository;
import code.bookings.domain.Room;
import code.bookings.domain.RoomRepository;
import code.bookings.domain.Screening;
import code.bookings.domain.exceptions.ScreeningDateException;
import code.bookings.infrastructure.exceptions.FilmNotFoundException;
import code.bookings.infrastructure.exceptions.RoomNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ScreeningCreateService {

    private final FilmRepository filmRepository;

    private final RoomRepository roomRepository;

    private final Clock clock;

    public Screening createScreening(CreateScreeningDto dto) {
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
            throw new ScreeningDateException("A screening date year must be current or next one");
        }
    }

    private Film getFilmOrThrow(CreateScreeningDto dto) {
        return filmRepository
                .findById(dto.filmId())
                .orElseThrow(FilmNotFoundException::new);
    }

    private Room getRoomOrThrow(UUID roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }
}
