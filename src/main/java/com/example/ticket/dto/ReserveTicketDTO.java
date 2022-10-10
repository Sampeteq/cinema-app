package com.example.ticket.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ReserveTicketDTO(@NotNull Long screeningId,
                               @NotNull String firstName,
                               @NotNull String lastName) {
}
