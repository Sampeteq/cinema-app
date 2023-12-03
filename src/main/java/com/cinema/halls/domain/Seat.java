package com.cinema.halls.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "seats")
@Getter
@ToString
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private Long hallId;

    protected Seat() {
    }

    public Seat(int rowNumber, int number, Long hallId) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.hallId = hallId;
    }
}