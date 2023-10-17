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

    private Long seatId;

    private Long userId;

    protected Ticket() {}

    public Ticket(TicketStatus status, Long screeningId, Long seatId, Long userId) {
        this.status = status;
        this.screeningId = screeningId;
        this.seatId = seatId;
        this.userId = userId;
    }

    public void cancel() {
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        this.status = TicketStatus.CANCELLED;
    }

    public boolean belongsTo(Long userId) {
        return this.userId.equals(userId);
    }
}