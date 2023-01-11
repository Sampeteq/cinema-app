package code.screenings;

import code.screenings.dto.ScreeningCreatingRequest;
import code.screenings.exception.FilmNotFoundException;
import code.screenings.exception.ScreeningDateException;
import code.screenings.exception.ScreeningRoomException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
class ScreeningFactory {

    private final ScreeningDateSpecification dateSpecification;

    private final FilmRepository filmRepository;

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    Screening createScreening(ScreeningCreatingRequest dto) {
        validateScreeningDate(dto.date());
        var film = getFilmOrThrow(dto.filmId());
        validateTimeAndRoomCollisionBetweenScreenings(dto.date(), film.getDurationInMinutes(), dto.roomId());
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

    private void validateTimeAndRoomCollisionBetweenScreenings(
            LocalDateTime screeningDate,
            int filmDurationInMinutes,
            UUID roomId
    ) {
        var screeningFinishDate = screeningDate.plusMinutes(filmDurationInMinutes);
        var isTimeAndRoomCollision = screeningRepository.existsByFinishDateGreaterThanAndDateLessThanAndRoomId(
                screeningDate,
                screeningFinishDate,
                roomId
        );
        if (isTimeAndRoomCollision) {
            throw new ScreeningRoomException("Time and room collision between screenings");
        }
    }

    private Film getFilmOrThrow(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    private ScreeningRoom getScreeningRoomOrThrow(UUID roomId) {
        return screeningRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ScreeningRoomNotFoundException(roomId));
    }
}
