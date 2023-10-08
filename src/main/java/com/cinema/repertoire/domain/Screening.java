package com.cinema.repertoire.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "screenings")
@Getter
@ToString(exclude = "seats")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne
    private Film film;

    private String roomId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    private List<Seat> seats;

    protected Screening() {}

    public Screening(
            LocalDateTime date,
            Film film,
            String roomId,
            List<Seat> seats
    ) {
        this.date = date;
        this.film = film;
        this.roomId = roomId;
        this.seats = seats;
    }

    public Optional<Seat> findSeat(int rowNumber, int seatNumber) {
        return this
                .seats
                .stream()
                .filter(s -> s.placedOn(rowNumber, seatNumber))
                .findFirst();
    }

    public boolean isEnded(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        var screeningEndDate = this.film.calculateScreeningEndDate(this.date);
        return screeningEndDate.isBefore(currentDate);
    }

    public void removeRoom() {
        this.roomId = null;
    }

    public LocalDateTime calculateEndDate() {
        return film.calculateScreeningEndDate(this.date);
    }
}
