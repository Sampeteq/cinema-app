package com.example.ticket.domain.dto;

import com.example.screening.domain.ScreeningValues;
import lombok.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Builder
public record ReserveTicketDTO(
        UUID screeningId,
        @NotBlank String firstName,

        @NotBlank String lastName,
        @Min(value = ScreeningValues.SCREENING_MIN_AGE) int age) {
}
