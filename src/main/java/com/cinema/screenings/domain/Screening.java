package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.tickets.domain.Ticket;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hall hall;

    @OneToMany(mappedBy = "screening", cascade = {CascadeType.PERSIST})
    private List<Ticket> tickets;

    protected Screening() {}

    public Screening(LocalDateTime date, Film film, Hall hall) {
        this.date = date;
        this.film = film;
        this.hall = hall;
    }

    public boolean collide(LocalDateTime otherScreeningDate, LocalDateTime otherScreeningEndDate) {
        var endDate = this.date.plusMinutes(film.getDurationInMinutes());
        return
                (!otherScreeningDate.isAfter(this.date) && !otherScreeningEndDate.isBefore(this.date)) ||
                        (otherScreeningDate.isAfter(this.date) && !otherScreeningDate.isAfter(endDate));
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
}
