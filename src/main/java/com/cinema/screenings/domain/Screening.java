package com.cinema.screenings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private LocalDateTime endDate;

    private Long filmId;

    private Long hallId;

    protected Screening() {}

    public Screening(LocalDateTime date, LocalDateTime endDate, Long filmId, Long hallId) {
        this.date = date;
        this.endDate = endDate;
        this.filmId = filmId;
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
