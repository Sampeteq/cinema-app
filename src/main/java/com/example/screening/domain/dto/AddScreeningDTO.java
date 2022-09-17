package com.example.screening.domain.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AddScreeningDTO(@NotNull UUID filmId,
                              @NotNull LocalDateTime date,
                              @NotNull Integer freeSeats,
                              @NotNull Integer minAge) {}
