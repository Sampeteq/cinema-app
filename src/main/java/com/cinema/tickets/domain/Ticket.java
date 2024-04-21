package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.util.UUID;

import static com.cinema.tickets.domain.TicketConstants.MIN_BOOKING_HOURS;
import static com.cinema.tickets.domain.TicketConstants.MIN_CANCELLATION_HOURS;

@AllArgsConstructor
@Getter
@ToString(exclude = "screening")
public class Ticket {
    private UUID id;
    private Screening screening;
    private Seat seat;
    private UUID userId;
    private int version;

    public void book(Clock clock, UUID userId) {
        if (screening.hoursLeftBeforeStart(clock) < MIN_BOOKING_HOURS) {
            throw new TicketBookTooLateException();
        }
        if (this.userId != null) {
            throw new TicketAlreadyBookedException();
        }
        this.userId = userId;
    }

    public void cancel(Clock clock) {
        if (screening.hoursLeftBeforeStart(clock) < MIN_CANCELLATION_HOURS) {
            throw new TicketCancelTooLateException();
        }
        this.userId = null;
    }
}