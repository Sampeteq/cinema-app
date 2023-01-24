package code.screenings;

import code.rooms.RoomFacade;
import code.rooms.dto.RoomDetails;
import code.screenings.dto.CreateScreeningDto;
import code.screenings.exception.FilmNotFoundException;
import code.screenings.exception.ScreeningDateException;
import code.rooms.exception.RoomException;
import code.rooms.exception.RoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
class ScreeningFactory {

    private final ScreeningDateSpecification dateSpecification;

    private final ScreeningRepository screeningRepository;

    private final FilmRepository filmRepository;

    private final RoomFacade roomFacade;

    Screening createScreening(CreateScreeningDto dto) {
        validateScreeningDate(dto.date());
        var film = getFilmOrThrow(dto);
        validateTimeAndRoomCollisionBetweenScreenings(dto.date(),  film.getDurationInMinutes(), dto.roomId());
        var roomDetails = getRoomDetailsOrThrow(dto.roomId());
        var screening = Screening.of(
                dto.date(),
                dto.minAge(),
                film,
                dto.roomId(),
                roomDetails
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
            throw new RoomException("Time and room collision between screenings");
        }
    }

    private RoomDetails getRoomDetailsOrThrow(UUID roomId) {
        return roomFacade
                .searchRoomDetails(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }
}
