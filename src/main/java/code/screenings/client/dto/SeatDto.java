package code.screenings.client.dto;

import lombok.With;

@With
public record SeatDto(
        Long id,
        int rowNumber,
        int number,
        String status
) {
}
