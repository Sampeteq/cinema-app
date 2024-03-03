package com.cinema.halls.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Positive;

@Embeddable
public record Seat(@Positive int rowNumber, @Positive int number) {}