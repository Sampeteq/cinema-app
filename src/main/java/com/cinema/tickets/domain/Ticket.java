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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;

@Entity
@Table(name = "tickets")
@Getter
@ToString(exclude = {"seat", "user"})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    private ScreeningSeat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    protected Ticket() {}

    public Ticket(TicketStatus status, ScreeningSeat seat, User user) {
        this.status = status;
        this.seat = seat;
        this.user = user;
    }

    public void cancel(TicketCancellingPolicy ticketCancellingPolicy, Clock clock) {
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        var timeToScreeningInHours = this.seat.getScreening().timeToScreeningInHours(clock);
        ticketCancellingPolicy.checkScreeningDate(timeToScreeningInHours);
        this.status = TicketStatus.CANCELLED;
        this.seat.markAsFree();
    }
}