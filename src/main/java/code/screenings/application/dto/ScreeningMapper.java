package code.screenings.application.dto;

import code.films.domain.Film;
import code.rooms.domain.Room;
import code.screenings.domain.Screening;
import code.screenings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ScreeningMapper {

    @Mapping(target = "filmTitle", source = "film", qualifiedByName = "filmToFilmTitle")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    ScreeningDto mapToDto(Screening screening);

    @Mapping(target = "filmTitle", source = "film", qualifiedByName = "filmToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    List<ScreeningDto> mapToDto(List<Screening> screening);

    @Named("filmToFilmTitle")
    static String filmToId(Film film) {
        return film.getTitle();
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
