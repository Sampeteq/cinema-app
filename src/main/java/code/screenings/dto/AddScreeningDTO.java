package code.screenings.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(
        @NotNull
        @Schema(type="string" , example = "2022-01-01 16:30")  LocalDateTime date,
        @NotNull
        Integer freeSeatsQuantity,
        @NotNull
        Integer minAge,
        @NotNull
        Long filmId,
        @NotNull
        UUID roomUuid
) {

}
