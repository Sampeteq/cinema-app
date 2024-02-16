package com.cinema.halls.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Seat(int rowNumber, int number) {}