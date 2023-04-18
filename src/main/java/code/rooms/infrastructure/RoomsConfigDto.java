package code.rooms.infrastructure;

import code.rooms.client.commands.CreateRoomCommand;

import java.util.List;

record RoomsConfigDto(List<CreateRoomCommand> rooms) {
}
