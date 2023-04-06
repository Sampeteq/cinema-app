package code.rooms.domain.commands;

import lombok.With;

import javax.validation.constraints.Positive;

@With
public record RoomCreateCommand(
        @Positive
        int number,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
