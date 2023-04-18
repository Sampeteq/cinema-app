package code.rooms.infrastructure;

import code.rooms.domain.client.commands.CreateRoomCommand;

import java.util.List;

record RoomsConfigDto(List<CreateRoomCommand> rooms) {
}
