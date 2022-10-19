package code.reservation.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ReserveScreeningTicketDTO(@NotNull Long screeningId,
                                        @NotNull String firstName,
                                        @NotNull String lastName) {
}
