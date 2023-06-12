package code.films.application.dto;

import lombok.With;

@With
public record SeatDto(
        Long id,
        int rowNumber,
        int number,
        boolean isFree
) {
}
