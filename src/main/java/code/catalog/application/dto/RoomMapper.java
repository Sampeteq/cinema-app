package code.catalog.application.dto;

import code.catalog.domain.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface RoomMapper {

    @Mapping(target = "seatsQuantity", source = "room" , qualifiedByName = "getSeatsQuantity")
    RoomDto mapToDto(Room room);

    @Named("getSeatsQuantity")
    static int getSeatsQuantity(Room room) {
        return room.getRowsQuantity() * room.getSeatsInOneRowQuantity();
    }
}
