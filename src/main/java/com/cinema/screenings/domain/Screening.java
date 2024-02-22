package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"film"})
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    private Long hallId;

    protected Screening() {}

    public Screening(LocalDateTime date, Film film, Long hallId) {
        this.date = date;
        this.film = film;
        this.hallId = hallId;
    }

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }
}
