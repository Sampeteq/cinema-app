package code.screenings.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ScreeningSeatDTO(
        UUID seatId,
        int rowNumber,
        int number,
        String status
) {
}
