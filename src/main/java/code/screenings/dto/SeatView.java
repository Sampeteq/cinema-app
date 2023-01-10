package code.screenings.dto;

import java.util.UUID;

public record SeatView(
        UUID id,
        int rowNumber,
        int number,
        String status
) {
}
