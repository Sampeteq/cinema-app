package com.cinema.halls.infrastructure.db;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Embeddable
@Table(name = "seat")
public record JpaSeat(@Positive int rowNumber, @Positive int number) {}