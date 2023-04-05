package code.films.applications.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreeningDto(
        UUID id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        int minAge,
        UUID filmId,
        UUID roomId,
        int freeSeats
) {
}
