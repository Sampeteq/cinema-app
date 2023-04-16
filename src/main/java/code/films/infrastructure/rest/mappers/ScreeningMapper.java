package code.films.infrastructure.rest.mappers;

import code.bookings.domain.Seat;
import code.films.domain.Film;
import code.films.domain.Screening;
import code.films.infrastructure.rest.dto.ScreeningDto;
import code.rooms.domain.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ScreeningMapper {

    @Mapping(target = "filmId", source = "film", qualifiedByName = "filmToUuid")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToUuid")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    ScreeningDto mapToDto(Screening screening);

    @Mapping(target = "filmId", source = "film", qualifiedByName = "filmToUuid")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToUuid")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    List<ScreeningDto> mapToDto(List<Screening> screening);

    @Named("filmToUuid")
    static UUID filmToUuid(Film film) {
        return film.getId();
    }

    @Named("roomToUuid")
    static UUID roomToUuid(Room room) {
        return room.getId();
    }

    @Named("seatsToFreeSeats")
    static int seatsToFreeSeats(List<Seat> seats) {
        return (int) seats
                .stream()
                .filter(Seat::isFree)
                .count();
    }
}
