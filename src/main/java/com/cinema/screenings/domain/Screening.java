package com.cinema.screenings.domain;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class Screening {
    private Long id;
    private LocalDateTime date;
    private LocalDateTime endDate;
    private Film film;
    private Hall hall;

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }
}
