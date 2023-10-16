package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long screeningId;

    private int rowNumber;

    private int seatNumber;

    private Long userId;

    protected Ticket() {}

    public Ticket(
            Long screeningId,
            int rowNumber,
            int seatNumber
    ) {
        this.screeningId = screeningId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public void makeActive(Long userId) {
        this.status = TicketStatus.ACTIVE;
        this.userId = userId;
    }

    public void makeCancelled() {
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        this.status = TicketStatus.CANCELLED;
    }

    public boolean belongsTo(Long userId) {
        return this.userId.equals(userId);
    }
}