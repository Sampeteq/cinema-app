package code.screenings.domain.dto;

public record ScreeningDetails(
        int timeToScreeningInHours,
        boolean isSeatAvailable
) {
}
