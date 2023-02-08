package code.screenings.application.dto;

public record SeatDetails(
        boolean isSeatAvailable,
        int timeToScreeningInHours
) {
}
