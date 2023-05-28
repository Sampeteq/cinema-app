package code.screenings.domain;

import code.films.domain.Film;
import code.rooms.domain.Room;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"film"})
@With
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    public static Screening create(LocalDateTime date, Film film, Room room) {
        return new Screening(
                null,
                date,
                film,
                room,
                new ArrayList<>()
        );
    }

    public static Screening create(LocalDateTime date, Film film) {
        return new Screening(
                null,
                date,
                film,
                null,
                new ArrayList<>()
        );
    }

    public void addSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void assignRoom(Room room) {
        this.room = room;
    }

    public void removeRoom() {
        this.room = null;
    }

    public boolean hasRoom() {
        return this.room != null;
    }

    public boolean isCollisionWith(LocalDateTime start, LocalDateTime finish) {
        return start.isBefore(this.finishDate()) && finish.isAfter(this.date);
    }

    public int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), date)
                .abs()
                .toHours();
    }

    public LocalDateTime startDate() {
        return date;
    }

    public LocalDateTime finishDate() {
        return date.plusMinutes(film.getDurationInMinutes());
    }

    public boolean isFinished(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return currentDate.isAfter(finishDate());
    }
}
