package com.cinema.tickets.domain;

import com.cinema.shared.exceptions.EntityNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "ticket_screening")
@Table(name = "tickets_screenings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "seats")
public class Screening {

    @Id
    private Long id;

    private LocalDateTime date;

    private String filmTitle;

    private String roomCustomId;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;

    public Screening(
            Long id,
            LocalDateTime date,
            String filmTitle,
            String roomCustomId,
            List<Seat> seats
    ) {
        this.id = id;
        this.date = date;
        this.filmTitle = filmTitle;
        this.roomCustomId = roomCustomId;
        this.seats = seats;
    }

    public static Screening create(
            Long id,
            LocalDateTime date,
            String filmTitle,
            String roomCustomId,
            List<Seat> seats
    ) {
        var screening = new Screening(id, date, filmTitle, roomCustomId, seats);
        seats.forEach(seat -> seat.assignScreening(screening));
        return screening;
    }

    public Seat findSeat(int rowNumber, int number) {
        return this
                .seats
                .stream()
                .filter(s -> s.placedOn(rowNumber, number))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
    }

    public long timeToScreeningInHours(LocalDateTime currentDate) {
        return Duration
                .between(currentDate, date)
                .abs()
                .toHours();
    }
}
