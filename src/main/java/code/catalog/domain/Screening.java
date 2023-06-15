package code.catalog.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.util.List;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private boolean isFinished;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    private Screening(LocalDateTime date, Film film, Room room, List<Seat> seats) {
        this.date = date;
        this.film = film;
        this.room = room;
        this.seats = seats;
    }

    public static Screening create(LocalDateTime date, Film film, Room room, List<Seat> seats) {
        var screening = new Screening(
                date,
                film,
                room,
                seats
        );
        seats.forEach(seat -> seat.assignScreening(screening));
        return screening;
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

    public boolean isFinished(Clock clock) {
        var currentDate = LocalDateTime.now(clock);
        return currentDate.isAfter(finishDate());
    }

    public void finish() {
        this.isFinished = true;
        this.room = null;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", date=" + date +
                ", isFinished=" + isFinished +
                ", film=" + film.getId() +
                ", room=" + room.getId() +
                '}';
    }

    private LocalDateTime finishDate() {
        return date.plusMinutes(film.getDurationInMinutes());
    }
}
