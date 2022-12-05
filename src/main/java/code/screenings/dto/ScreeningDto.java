package code.screenings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ScreeningDto(
        UUID id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime date,
        int freeSeats,
        int minAge,
        UUID filmId,
        UUID roomId,
        List<ScreeningSeatDto> seats
) {
}