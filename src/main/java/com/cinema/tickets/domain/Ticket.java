package com.cinema.tickets.domain;

import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
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

    @OneToOne
    private ScreeningSeat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    protected Ticket() {}

    public Ticket(Status status, ScreeningSeat seat, User user) {
        this.status = status;
        this.seat = seat;
        this.user = user;
    }

    public void cancel(TicketCancellingPolicy ticketCancellingPolicy, Clock clock) {
        if (status.equals(Ticket.Status.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        var timeToScreeningInHours = this.seat.getScreening().timeToScreeningInHours(clock);
        ticketCancellingPolicy.checkScreeningDate(timeToScreeningInHours);
        this.status = Ticket.Status.CANCELLED;
        this.seat.markAsFree();
    }
}