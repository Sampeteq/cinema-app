package code.screenings.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreeningDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        Long filmId,
        Long roomId,
        int freeSeats
) {
}
