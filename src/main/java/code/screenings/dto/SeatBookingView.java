package code.screenings.dto;

import java.util.UUID;

public record SeatBookingView(
        UUID id,

        String firstName,

        String lastName,

        SeatView seat
) {
}
