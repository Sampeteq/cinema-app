package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private Long screeningId;

    @Embedded
    private Seat seat;

    private Long userId;

    protected Ticket() {}

    public Ticket(Long screeningId, Seat seat) {
        this.screeningId = screeningId;
        this.seat = seat;
    }

    public void assignUserId(Long userId) {
        if (this.userId != null) {
            throw new TicketAlreadyBookedException();
        }
        this.userId = userId;
    }

    public void removeUserId() {
        this.userId = null;
    }
}