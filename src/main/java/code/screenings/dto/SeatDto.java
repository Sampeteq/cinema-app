package code.screenings.dto;

import java.util.UUID;

public record SeatDto(
        UUID id,
        int rowNumber,
        int number,
        String status
) {
}
