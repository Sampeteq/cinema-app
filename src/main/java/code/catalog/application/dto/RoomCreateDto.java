package code.catalog.application.dto;

import lombok.With;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
