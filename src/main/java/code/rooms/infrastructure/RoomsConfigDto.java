package code.rooms.infrastructure;

import code.rooms.domain.commands.RoomCreateCommand;

import java.util.List;

record RoomsConfigDto(List<RoomCreateCommand> rooms) {
}
