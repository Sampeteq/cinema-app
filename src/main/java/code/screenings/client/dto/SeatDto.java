package code.screenings.client.dto;

import lombok.With;

import java.util.UUID;

@With
public record SeatDto(
        Long id,
        int rowNumber,
        int number,
        String status
) {
}
