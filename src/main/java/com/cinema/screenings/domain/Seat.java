package com.cinema.screenings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(value = EnumType.STRING)
    private SeatStatus status;

    protected Seat() {
    }

    public Seat(int rowNumber, int number, SeatStatus status) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.status = status;
    }

    public void take() {
        this.status = SeatStatus.TAKEN;
    }

    public void free() {
        this.status = SeatStatus.FREE;
    }
}