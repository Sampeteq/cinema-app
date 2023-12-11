package com.cinema.screenings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "screenings_seats")
@Getter
public class ScreeningSeat {

    @Id
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    protected ScreeningSeat() {
    }

    public ScreeningSeat(Long id, int rowNumber, int number, boolean isFree) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }
}
