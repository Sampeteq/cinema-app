package com.cinema.halls;

import jakarta.persistence.Embeddable;

@Embeddable
public record Seat(int rowNumber, int number) {}