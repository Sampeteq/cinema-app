package code.films.application.dto;

import code.films.domain.FilmScreeningRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface FilmScreeningRoomMapper {

    @Mapping(target = "seatsQuantity", source = "room" , qualifiedByName = "getSeatsQuantity")
    FilmScreeningRoomDto mapToDto(FilmScreeningRoom room);

    @Named("getSeatsQuantity")
    static int getSeatsQuantity(FilmScreeningRoom room) {
        return room.getRowsQuantity() * room.getSeatsInOneRowQuantity();
    }
}
