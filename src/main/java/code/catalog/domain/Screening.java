package code.catalog.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    private Screening(
            LocalDateTime date,
            LocalDateTime endDate,
            Film film,
            Room room,
            List<Seat> seats
    ) {
        this.date = date;
        this.endDate = endDate;
        this.film = film;
        this.room = room;
        this.seats = seats;
    }

    public static Screening create(LocalDateTime date, Film film, Room room, List<Seat> seats) {
        var endDate = date.plusMinutes(film.getDurationInMinutes());
        var screening = new Screening(
                date,
                endDate,
                film,
                room,
                seats
        );
        seats.forEach(seat -> seat.assignScreening(screening));
        return screening;
    }

    public boolean collide(LocalDateTime start, LocalDateTime finish) {
        return start.isBefore(this.endDate) && finish.isAfter(this.date);
    }

    public int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), date)
                .abs()
                .toHours();
    }

    public void removeRoom() {
        this.room = null;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", date=" + date +
                ", end date=" + endDate +
                ", film=" + film.getId() +
                ", room=" + room.getId() +
                '}';
    }
}
