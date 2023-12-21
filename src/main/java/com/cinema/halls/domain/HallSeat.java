package com.cinema.halls.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "halls_seats")
@Getter
@ToString
public class HallSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    protected HallSeat() {
    }

    public HallSeat(int rowNumber, int number) {
        this.rowNumber = rowNumber;
        this.number = number;
    }
}