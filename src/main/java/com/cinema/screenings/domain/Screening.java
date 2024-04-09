package com.cinema.screenings.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class Screening {
    private UUID id;
    private LocalDateTime date;
    private LocalDateTime endDate;
    private UUID filmId;
    private UUID hallId;

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }
}
