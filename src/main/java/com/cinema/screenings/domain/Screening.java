package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.exceptions.TicketNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString(exclude = {"film", "hall", "tickets"})
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hall hall;

    @OneToMany(mappedBy = "screening", cascade = {CascadeType.PERSIST})
    private List<Ticket> tickets;

    protected Screening() {}

    public Screening(LocalDateTime date, LocalDateTime endDate, Film film, Hall hall) {
        this.date = date;
        this.endDate = endDate;
        this.film = film;
        this.hall = hall;
    }

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }

    public void assignTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Ticket findTicketBySeatId(Long seatId) {
        return tickets
                .stream()
                .filter(ticket -> ticket.getSeat().getId().equals(seatId))
                .findFirst()
                .orElseThrow(TicketNotFoundException::new);
    }
}
