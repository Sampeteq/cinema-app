package com.cinema.screenings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "screenings_seats")
@Getter
public class ScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    private Long screeningId;

    protected ScreeningSeat() {
    }

    public ScreeningSeat(int rowNumber, int number, boolean isFree, Long screeningId) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
        this.screeningId = screeningId;
    }

    public void markAsNotFree() {
        this.isFree = false;
    }

    public void markAsFree() {
        this.isFree = true;
    }
}
