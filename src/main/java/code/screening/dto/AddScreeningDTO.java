package code.screening.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
public record AddScreeningDTO(@NotNull Long filmId,
                              @NotNull LocalDateTime date,
                              @NotNull Integer freeSeats,
                              @NotNull Integer minAge) {
}
