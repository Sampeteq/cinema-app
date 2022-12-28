package code.screenings.dto;

import java.util.UUID;

public record ScreeningTicketDto(
        UUID ticketId,

//        UUID screeningId,

//        UUID seatId,

        String firstName,

        String lastName,

        ScreeningSeatDto seat
) {
}
