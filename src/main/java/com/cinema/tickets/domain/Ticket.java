package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString(exclude = "screening")
public class Ticket {
    private UUID id;
    private Screening screening;
    private Seat seat;
    private UUID userId;
    private int version;

    public void assignUser(UUID userId) {
        if (this.userId != null) {
            throw new TicketAlreadyBookedException();
        }
        this.userId = userId;
    }

    public void removeUser() {
        this.userId = null;
    }
}