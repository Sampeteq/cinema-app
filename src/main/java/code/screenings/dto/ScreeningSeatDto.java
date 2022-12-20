package code.screenings.dto;

import java.util.UUID;

public record ScreeningSeatDto(
        UUID seatId,
        int rowNumber,
        int number,
        String status
) {
}
