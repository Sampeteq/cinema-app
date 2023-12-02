package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "tickets")
@Getter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @OneToOne
    private Seat seat;

    private Long userId;

    protected Ticket() {}

    public Ticket(TicketStatus status, Seat seat, Long userId) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public void cancel() {
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        this.status = TicketStatus.CANCELLED;
    }
}