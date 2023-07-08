package code.catalog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@With
public record ScreeningCreateDto(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull
        @Schema(type = "string", example = "2022-01-01T16:30")
        LocalDateTime date,

        @NotNull
        Long filmId
) {
}
