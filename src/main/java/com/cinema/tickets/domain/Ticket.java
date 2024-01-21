package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.users.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;

@Entity
@Getter
@ToString(exclude = {"user"})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Status {
        BOOKED,
        CANCELLED
    }

    @Enumerated(EnumType.STRING)
    private Ticket.Status status;

    @Version
    private int version;

    @ManyToOne
    private Screening screening;

    @OneToOne
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    protected Ticket() {}

    public Ticket(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }

    public Ticket(Ticket.Status status, Screening screening, Seat seat, User user) {
        this.status = status;
        this.screening = screening;
        this.seat = seat;
        this.user = user;
    }

    public void book(User user) {
        if (status != null && status.equals(Ticket.Status.BOOKED)) {
            throw new TicketAlreadyBookedException();
        }
        this.status = Ticket.Status.BOOKED;
        this.user = user;
    }

    public void cancel(TicketCancellingPolicy ticketCancellingPolicy, Clock clock) {
        if (status.equals(Ticket.Status.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        var timeToScreeningInHours = screening.timeToScreeningInHours(clock);
        ticketCancellingPolicy.checkScreeningDate(timeToScreeningInHours);
        this.status = Ticket.Status.CANCELLED;
    }
}