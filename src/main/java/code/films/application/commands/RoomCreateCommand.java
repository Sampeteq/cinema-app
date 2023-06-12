package code.films.application.commands;

import lombok.With;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@With
public record RoomCreateCommand(
        @NotNull
        String customId,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
