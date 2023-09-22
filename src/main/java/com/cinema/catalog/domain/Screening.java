package com.cinema.catalog.domain;

import com.cinema.shared.exceptions.EntityNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    private String roomCustomId;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;

    protected Screening() {}

    private Screening(
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

    public static Screening create(
            LocalDateTime date,
            Film film,
            String roomCustomId,
            List<Seat> seats
    ) {
        var endDate = film.calculateScreeningEndDate(date);
        var screening = new Screening(date, endDate, film, roomCustomId, seats);
        seats.forEach(seat -> seat.assignScreening(screening));
        return screening;
    }

    public Seat findSeat(int rowNumber, int seatNumber) {
        return this
                .seats
                .stream()
                .filter(s -> s.placedOn(rowNumber, seatNumber))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }
}
