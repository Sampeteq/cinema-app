package com.cinema.catalog.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

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

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    private String roomCustomId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_id")
    private List<Seat> seats;

    protected Screening() {}

    public Screening(
            LocalDateTime date,
            LocalDateTime endDate,
            Film film,
            String roomCustomId,
            List<Seat> seats
    ) {
        this.date = date;
        this.endDate = endDate;
        this.film = film;
        this.roomCustomId = roomCustomId;
        this.seats = seats;
    }

    public Optional<Seat> findSeat(int rowNumber, int seatNumber) {
        return this
                .seats
                .stream()
                .filter(s -> s.placedOn(rowNumber, seatNumber))
                .findFirst();
    }

    public void removeRoom() {
        this.roomCustomId = null;
    }
}
