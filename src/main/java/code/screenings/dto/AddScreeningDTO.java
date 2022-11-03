package code.screenings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(@NotNull Long filmId,

                              @NotNull UUID roomUuid,
                              @NotNull @Schema(type="string" , example = "2022-01-01 16:30")  LocalDateTime date,
                              @NotNull Integer minAge,
                              @NotNull Integer freeSeatsQuantity) {
}
