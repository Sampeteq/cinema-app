package code.films.dto;

import java.util.UUID;

public record SeatDto(
        UUID id,
        int rowNumber,
        int number,
        String status
) {
}
