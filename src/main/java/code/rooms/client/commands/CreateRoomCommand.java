package code.rooms.client.commands;

import lombok.With;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@With
public record CreateRoomCommand(
        @NotNull
        String customId,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
