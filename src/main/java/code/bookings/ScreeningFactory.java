package code.bookings;

import code.bookings.dto.CreateScreeningDto;
import code.bookings.exception.FilmNotFoundException;
import code.bookings.exception.ScreeningDateException;
import code.bookings.exception.ScreeningRoomException;
import code.bookings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
class ScreeningFactory {

    private final ScreeningDateSpecification dateSpecification;

    private final ScreeningRepository screeningRepository;

    private final RoomRepository roomRepository;

    private final FilmRepository filmRepository;

    Screening createScreening(CreateScreeningDto dto) {
        validateScreeningDate(dto.date());
        var film = getFilmOrThrow(dto);
        validateTimeAndRoomCollisionBetweenScreenings(dto.date(),  film.getDurationInMinutes(), dto.roomId());
        var room = getScreeningRoomOrThrow(dto.roomId());
        var screening = Screening.of(
                dto.date(),
                dto.minAge(),
                film,
                room
        );
        return screeningRepository.save(screening);
    }

    private void validateScreeningDate(LocalDateTime date) {
        if (!dateSpecification.isSatisfyBy(date)) {
            if (dateSpecification instanceof CurrentOrNextOneYearScreeningDateSpecification) {
                throw new ScreeningDateException("A screening date year must be current or next one");
            } else {
                throw new IllegalArgumentException("Unsupported screening date specification");
            }
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
                .anyMatch(screening -> screening.IsTimeCollision(screeningDate, screeningFinishDate));
        if (isTimeAndRoomCollision) {
            throw new ScreeningRoomException("Time and room collision between screenings");
        }
    }

    private Room getScreeningRoomOrThrow(UUID roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(ScreeningRoomNotFoundException::new);
    }
}
