package code.screenings.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ScreeningDto(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        String filmTitle,
        Long roomId,
        int freeSeats
) {
}
