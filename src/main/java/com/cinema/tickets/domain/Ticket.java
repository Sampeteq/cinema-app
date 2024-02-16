package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.users.domain.User;
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

    @ManyToOne
    private User user;

    protected Ticket() {}

    public Ticket(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }

    public Ticket(Screening screening, Seat seat, User user) {
        this.screening = screening;
        this.seat = seat;
        this.user = user;
    }

    public void book(TicketBookingPolicy ticketBookingPolicy, Clock clock, User user) {
        ticketBookingPolicy.checkIfBookingIsPossible(screening.hoursLeftBeforeStart(clock));
        if (this.user != null) {
            throw new TicketAlreadyBookedException();
        }
        this.user = user;
    }

    public void cancel(TicketCancellingPolicy ticketCancellingPolicy, Clock clock) {
        ticketCancellingPolicy.checkIfCancellingIsPossible(screening.hoursLeftBeforeStart(clock));
        this.user = null;
    }
}