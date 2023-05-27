package code.rooms.application.commands;

import lombok.With;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@With
public record RoomCreationCommand(
        @NotNull
        String customId,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
