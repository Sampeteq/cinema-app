package com.example.ticket.domain.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ReserveTicketDTO(@NotNull Long screeningId,
                               @NotNull String firstName,
                               @NotNull String lastName,
                               @NotNull Integer age) {
}
