package code.films.application.dto;

import code.films.domain.Film;
import code.films.domain.FilmScreening;
import code.rooms.domain.Room;
import code.films.domain.FilmScreeningSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface FilmScreeningMapper {

    @Mapping(target = "filmTitle", source = "film", qualifiedByName = "filmToFilmTitle")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    FilmScreeningDto mapToDto(FilmScreening screening);

    @Mapping(target = "filmTitle", source = "film", qualifiedByName = "filmToId")
    @Mapping(target = "roomId", source = "room", qualifiedByName = "roomToId")
    @Mapping(target = "freeSeats", source = "seats", qualifiedByName = "seatsToFreeSeats")
    List<FilmScreeningDto> mapToDto(List<FilmScreening> screening);

    @Named("filmToFilmTitle")
    static String filmToId(Film film) {
        return film.getTitle();
    }

    @Named("roomToId")
    static Long roomToId(Room room) {
        return room.getId();
    }

    @Named("seatsToFreeSeats")
    static int seatsToFreeSeats(List<FilmScreeningSeat> seats) {
        return (int) seats
                .stream()
                .filter(FilmScreeningSeat::isFree)
                .count();
    }
}
