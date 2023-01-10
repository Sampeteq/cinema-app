package code.screenings.dto;

import lombok.With;

import javax.validation.constraints.Positive;

@With
public record ScreeningRoomCreatingRequest(
        @Positive
        int number,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
