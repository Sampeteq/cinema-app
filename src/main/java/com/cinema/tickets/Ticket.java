package com.cinema.tickets;

import com.cinema.halls.Seat;
import com.cinema.tickets.exceptions.TicketAlreadyBookedException;
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
class Ticket {

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

    Ticket(Long screeningId, Seat seat) {
        this.screeningId = screeningId;
        this.seat = seat;
    }

    Ticket(Long screeningId, Seat seat, Long userId) {
        this.screeningId = screeningId;
        this.seat = seat;
        this.userId = userId;
    }

    void assignUserId(Long userId) {
        if (this.userId != null) {
            throw new TicketAlreadyBookedException();
        }
        this.userId = userId;
    }

    void removeUserId() {
        this.userId = null;
    }
}