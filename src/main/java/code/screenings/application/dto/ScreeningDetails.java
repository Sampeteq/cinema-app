package code.screenings.application.dto;

public record ScreeningDetails(
        int timeToScreeningInHours,
        boolean isSeatAvailable
) {
}
