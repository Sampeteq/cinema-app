package code.films.application.dto;

public record SeatDetails(
        boolean isSeatAvailable,
        int timeToScreeningInHours
) {
}
