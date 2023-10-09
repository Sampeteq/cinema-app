package com.cinema.repertoire.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    @Enumerated(value = EnumType.STRING)
    private SeatStatus status;

    public Seat(int rowNumber, int number, SeatStatus status) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.status = status;
    }

    public boolean placedOn(int rowNumber, int seatNumber) {
        return this.rowNumber == rowNumber && this.number == seatNumber;
    }

    public void makeTaken() {
        this.status = SeatStatus.TAKEN;
    }

    public void makeFree() {
        this.status = SeatStatus.FREE;
    }
}