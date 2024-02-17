package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;

@Entity
@Getter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @ManyToOne
    private Screening screening;

    @Embedded
    private Seat seat;

    private Long userId;

    protected Ticket() {}

    public Ticket(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }

    public Ticket(Screening screening, Seat seat, Long userId) {
        this.screening = screening;
        this.seat = seat;
        this.userId = userId;
    }

    public void book(TicketBookingPolicy ticketBookingPolicy, Clock clock, Long userId) {
        ticketBookingPolicy.checkIfBookingIsPossible(screening.hoursLeftBeforeStart(clock));
        if (this.userId != null) {
            throw new TicketAlreadyBookedException();
        }
        this.userId = userId;
    }

    public void cancel(TicketCancellingPolicy ticketCancellingPolicy, Clock clock) {
        ticketCancellingPolicy.checkIfCancellingIsPossible(screening.hoursLeftBeforeStart(clock));
        this.userId = null;
    }
}