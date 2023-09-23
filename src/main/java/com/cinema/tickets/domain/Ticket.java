package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private String filmTitle;

    private LocalDateTime screeningDate;

    private Long screeningId;

    private String roomCustomId;

    private int rowNumber;

    private int seatNumber;

    private Long userId;

    protected Ticket() {}

    public Ticket(
            String filmTitle,
            Long screeningId,
            LocalDateTime screeningDate,
            String roomCustomId,
            int rowNumber,
            int seatNumber
    ) {
        this.filmTitle = filmTitle;
        this.screeningId = screeningId;
        this.screeningDate = screeningDate;
        this.roomCustomId = roomCustomId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    public void book(Clock clock, Long userId) {
        if (timeToScreeningInHours(clock) < 1) {
            throw new TicketBookTooLateException();
        }
        this.status = TicketStatus.ACTIVE;
        this.userId = userId;
    }

    public void cancel(Clock clock) {
        if (timeToScreeningInHours(clock) < 24) {
            throw new TicketCancelTooLateException();
        }
        if (status.equals(TicketStatus.CANCELLED)) {
            throw new TicketAlreadyCancelledException();
        }
        this.status = TicketStatus.CANCELLED;
    }

    private long timeToScreeningInHours(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.screeningDate)
                .abs()
                .toHours();
    }
}