package code.films.domain;

import code.films.application.dto.CreateScreeningDto;
import code.films.domain.*;
import code.films.domain.exceptions.ScreeningDateException;
import code.films.domain.exceptions.TimeAndRoomCollisionException;
import code.films.infrastructure.exceptions.FilmNotFoundException;
import code.films.infrastructure.exceptions.RoomNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ScreeningFactory {

    private final ScreeningRepository screeningRepository;

    private final FilmRepository filmRepository;

    private final RoomRepository roomRepository;

    private final Clock clock;

    public Screening createScreening(CreateScreeningDto dto) {
        validateScreeningDate(dto.date());
        var film = getFilmOrThrow(dto);
        validateTimeAndRoomCollisionBetweenScreenings(dto.date(), film.getDurationInMinutes(), dto.roomId());
        var room = getRoomOrThrow(dto.roomId());
        var screening = Screening.of(
                dto.date(),
                dto.minAge(),
                film,
                room
        );
        return screeningRepository.save(screening);
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

    private void validateTimeAndRoomCollisionBetweenScreenings(
            LocalDateTime screeningDate,
            int filmDurationInMinutes,
            UUID roomId
    ) {
        var screeningFinishDate = screeningDate.plusMinutes(filmDurationInMinutes);
        var isTimeAndRoomCollision = screeningRepository
                .findByRoomId(roomId)
                .stream()
                .anyMatch(screening -> screening.isTimeCollision(screeningDate, screeningFinishDate));
        if (isTimeAndRoomCollision) {
            throw new TimeAndRoomCollisionException();
        }
    }

    private Room getRoomOrThrow(UUID roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }
}
