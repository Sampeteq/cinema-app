package code.screenings;

import code.films.FilmFacade;
import code.screenings.dto.CreateScreeningDto;
import code.screenings.exception.ScreeningDateException;
import code.screenings.exception.ScreeningRoomException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
class ScreeningFactory {

    private final ScreeningDateSpecification dateSpecification;

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final FilmFacade filmFacade;

    Screening createScreening(CreateScreeningDto dto) {
        validateScreeningDate(dto.date());
        var filmDuration = filmFacade.searchFilmDuration(dto.filmId());
        validateTimeAndRoomCollisionBetweenScreenings(dto.date(), dto.roomId(), filmDuration);
        var room = getScreeningRoomOrThrow(dto.roomId());
        var screening = Screening.of(
                dto.date(),
                dto.minAge(),
                dto.filmId(),
                filmDuration,
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
            UUID roomId,
            int filmDurationInMinutes
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

    private ScreeningRoom getScreeningRoomOrThrow(UUID roomId) {
        return screeningRoomRepository
                .findById(roomId)
                .orElseThrow(ScreeningRoomNotFoundException::new);
    }
}
