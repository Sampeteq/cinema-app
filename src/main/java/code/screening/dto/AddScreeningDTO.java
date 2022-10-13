package code.screening.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(@NotNull Long filmId,

                              @NotNull UUID roomUuid,
                              @NotNull LocalDateTime date,
                              @NotNull Integer freeSeats,
                              @NotNull Integer minAge) {
}
