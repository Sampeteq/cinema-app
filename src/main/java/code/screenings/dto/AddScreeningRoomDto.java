package code.screenings.dto;

import lombok.With;

import javax.validation.constraints.Positive;

@With
public record AddScreeningRoomDto(
        @Positive
        int number,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
