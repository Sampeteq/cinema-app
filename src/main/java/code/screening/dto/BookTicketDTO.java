package code.screening.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record BookTicketDTO(@NotNull Long screeningId,
                            @NotNull String firstName,
                            @NotNull String lastName) {
}
