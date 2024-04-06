package com.cinema.tickets.domain;

import com.cinema.halls.domain.Seat;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(exclude = "screening")
public class Ticket {
    private Long id;
    private Screening screening;
    private Seat seat;
    private User user;
    private int version;

    public void assignUser(User user) {
        if (this.user != null) {
            throw new TicketAlreadyBookedException();
        }
        this.user = user;
    }

    public void removeUser() {
        this.user = null;
    }
}