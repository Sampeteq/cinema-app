package com.cinema.screenings.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private Screening screening;

    protected ScreeningSeat() {
    }

    public ScreeningSeat(int rowNumber, int number, boolean isFree, Screening screening) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
        this.screening = screening;
    }

    public void markAsNotFree() {
        if (!this.isFree) {
            throw new TicketAlreadyExistsException();
        }
        this.isFree = false;
    }

    public void markAsFree() {
        this.isFree = true;
    }
}
