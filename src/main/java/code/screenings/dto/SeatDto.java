package code.screenings.dto;

import lombok.With;

import java.util.UUID;

@With
public record SeatDto(
        UUID id,
        int rowNumber,
        int number,
        String status
) {
}
