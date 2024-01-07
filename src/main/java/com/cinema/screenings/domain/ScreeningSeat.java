package com.cinema.screenings.domain;

import com.cinema.halls.domain.HallSeat;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExistsException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "screenings_seats")
@Getter
public class ScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isFree;

    @OneToOne
    private HallSeat hallSeat;

    @ManyToOne
    private Screening screening;

    protected ScreeningSeat() {
    }

    public ScreeningSeat(boolean isFree, HallSeat hallSeat, Screening screening) {
        this.isFree = isFree;
        this.hallSeat = hallSeat;
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
