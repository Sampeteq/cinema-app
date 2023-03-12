package code.films.application.dto;

import java.util.UUID;

public record SeatDetails(
        boolean isSeatAvailable,
        int timeToScreeningInHours,

        UUID screeningId
) {
}
