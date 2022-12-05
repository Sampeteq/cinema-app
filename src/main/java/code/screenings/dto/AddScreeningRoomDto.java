package code.screenings.dto;

import lombok.Builder;
import lombok.With;

import javax.validation.constraints.Positive;

@Builder
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
