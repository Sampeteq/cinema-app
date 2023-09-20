package com.cinema.catalog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "screening")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne
    private Screening screening;

    private Seat(int rowNumber, int number, boolean isFree) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }

    public static Seat create(int rowNumber, int number) {
        final var isFree = true;
        return new Seat(
                rowNumber,
                number,
                isFree
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }

    public boolean placedOn(int rowNumber, int seatNumber) {
        return this.rowNumber == rowNumber && this.number == seatNumber;
    }

    public void makeNotFree() {
        this.isFree = false;
    }

    public void makeFree() {
        this.isFree = true;
    }
}