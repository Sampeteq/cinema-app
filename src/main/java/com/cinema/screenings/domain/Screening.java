package com.cinema.screenings.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "screenings")
@Getter
@ToString(exclude = "seats")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private Long filmId;

    private Long hallId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "screening_id")
    private List<ScreeningSeat> seats;

    protected Screening() {}

    public Screening(LocalDateTime date, Long filmId, Long hallId, List<ScreeningSeat> seats) {
        this.date = date;
        this.filmId = filmId;
        this.hallId = hallId;
        this.seats = seats;
    }
}
