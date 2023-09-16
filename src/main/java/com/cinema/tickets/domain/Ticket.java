package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@ToString(exclude = "seat")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    private Seat seat;

    private Long userId;

    protected Ticket() {}

    private Ticket(TicketStatus status, Long userId, Seat seat) {
        this.status = status;
        this.seat = seat;
        this.userId = userId;
    }

    public static Ticket book(
            LocalDateTime currentDate,
            Screening screening,
            int rowNumber,
            int seatNumber,
            Long userId
    ) {
        var foundSeat = screening.findSeat(rowNumber, seatNumber);
        var ticket = new Ticket(
                TicketStatus.ACTIVE,
                userId,
                foundSeat
        );
        foundSeat.addTicket(currentDate, ticket);
        return ticket;
    }

    public void cancel(LocalDateTime currentDate) {
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        this.seat.removeTicket(currentDate);
        this.status = TicketStatus.CANCELLED;
    }
}