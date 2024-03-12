package com.cinema.screenings.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime endDate;

    @NotNull
    private Long filmId;

    @NotNull
    private Long hallId;

    protected Screening() {}

    public Screening(LocalDateTime date, Long filmId, Long hallId) {
        this.date = date;
        this.filmId = filmId;
        this.hallId = hallId;
    }

    public Screening(LocalDateTime date, LocalDateTime endDate, Long hallId) {
        this.date = date;
        this.endDate = endDate;
        this.hallId = hallId;
    }

    public void assignEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public long hoursLeftBeforeStart(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return Duration
                .between(currentDate, this.date)
                .abs()
                .toHours();
    }
}
