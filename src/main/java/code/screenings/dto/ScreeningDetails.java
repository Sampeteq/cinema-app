package code.screenings.dto;

public record ScreeningDetails(
        int timeToScreeningInHours,
        boolean isSeatAvailable
) {
}
