package code.screening.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ScreeningDTO(
        Long id,
        LocalDateTime date,
        int freeSeats,
        int minAge,
        Long filmId,
        UUID roomUuid) {
}
