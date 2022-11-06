package code.bookings.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record BookScreeningTicketDTO(@NotNull Long screeningId,
                                     @NotNull String firstName,
                                     @NotNull String lastName) {
}
