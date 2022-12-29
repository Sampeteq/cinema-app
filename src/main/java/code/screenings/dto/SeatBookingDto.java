package code.screenings.dto;

import java.util.UUID;

public record SeatBookingDto(
        UUID id,

        String firstName,

        String lastName,

        SeatDto seat
) {
}
