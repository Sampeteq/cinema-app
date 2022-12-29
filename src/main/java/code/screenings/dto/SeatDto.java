package code.screenings.dto;

import java.util.UUID;

public record SeatDto(
        UUID seatId,
        int rowNumber,
        int number,
        String status,
        UUID screeningId
) {
}
