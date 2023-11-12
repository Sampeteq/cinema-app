package com.cinema.screenings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Getter
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Long filmId;

    private String roomId;

    protected Screening() {}

    public Screening(LocalDateTime date, Long filmId, String roomId
    ) {
        this.date = date;
        this.filmId = filmId;
        this.roomId = roomId;
    }

    public void removeRoom() {
        this.roomId = null;
    }
}
