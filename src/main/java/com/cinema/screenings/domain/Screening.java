package com.cinema.screenings.domain;

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
    private Long filmId;
    private Long hallId;

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }
}
