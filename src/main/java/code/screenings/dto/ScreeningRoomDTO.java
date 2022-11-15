package code.screenings.dto;

import java.util.UUID;

public record ScreeningRoomDTO(
        UUID id,
        int number,
        int freeSeats
) {
}
