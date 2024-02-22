package com.cinema.tickets;

import com.cinema.halls.Seat;
import com.cinema.screenings.Screening;
import com.cinema.tickets.exceptions.TicketAlreadyBookedException;
import jakarta.persistence.*;
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

    @ManyToOne
    private Screening screening;

    @Embedded
    private Seat seat;

    private Long userId;

    protected Ticket() {}

    Ticket(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }

    Ticket(Screening screening, Seat seat, Long userId) {
        this.screening = screening;
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