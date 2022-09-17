package com.example.ticket.domain.dto;

import com.example.screening.domain.ScreeningId;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record ReserveTicketDTO(@NotNull ScreeningId screeningId,
                               @NotNull String firstName,
                               @NotNull String lastName,
                               @NotNull Integer age) {}
