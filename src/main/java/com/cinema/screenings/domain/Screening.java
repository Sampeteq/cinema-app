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

    private String filmTitle;

    private String roomId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    private List<Seat> seats;

    protected Screening() {}

    public Screening(
            LocalDateTime date,
            String filmTitle,
            String roomId,
            List<Seat> seats
    ) {
        this.date = date;
        this.filmTitle = filmTitle;
        this.roomId = roomId;
        this.seats = seats;
    }

    public boolean hasSeat(Long id) {
        return this
                .seats
                .stream()
                .anyMatch(seat -> seat.hasId(id));
    }

    public Optional<Seat> findSeat(Long id) {
        return this
                .seats
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public void removeRoom() {
        this.roomId = null;
    }
}
