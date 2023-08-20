package code.catalog.application.dto;

import lombok.With;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@With
public record RoomCreateDto(
        @NotNull
        String customId,
        @Positive
        int rowsNumber,
        @Positive
        int rowSeatsNumber
) {
}
