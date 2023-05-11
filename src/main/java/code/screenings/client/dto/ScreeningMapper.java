package code.screenings.client.dto;

import code.films.domain.Film;
import code.rooms.domain.Room;
import code.screenings.domain.Screening;
import code.screenings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ScreeningMapper {

    @Mapping(target = "filmId", source = "film", qualifiedByName = "filmToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    ScreeningDto mapToDto(Screening screening);

    @Mapping(target = "filmId", source = "film", qualifiedByName = "filmToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    List<ScreeningDto> mapToDto(List<Screening> screening);

    @Named("filmToId")
    static Long filmToId(Film film) {
        return film.getId();
    }

    @Named("roomToId")
    static Long roomToId(Room room) {
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
