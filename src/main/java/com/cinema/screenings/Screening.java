package com.cinema.screenings;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    private String filmTitle;

    private Long hallId;

    protected Screening() {}

    public Screening(LocalDateTime date, String filmTitle, Long hallId) {
        this.date = date;
        this.filmTitle = filmTitle;
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
