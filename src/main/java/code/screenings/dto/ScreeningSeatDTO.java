package code.screenings.dto;

import code.screenings.ScreeningSeatStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ScreeningSeatDTO(
        UUID seatId,
        int rowNumber,
        int number,
        ScreeningSeatStatus status
) {
}
