package code.screenings.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@With
public record ScreeningCreateCommand(
        @NotNull
        @Schema(type = "string", example = "2022-01-01T16:30")
        LocalDateTime date,
        @NotNull
        Long filmId
) {
}