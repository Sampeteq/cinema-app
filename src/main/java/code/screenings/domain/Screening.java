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
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    public static Screening of(LocalDateTime date, Film film, Room room) {
        return new Screening(
                null,
                date,
                film,
                room,
                new ArrayList<>()
        );
    }

    public void addSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public boolean isCollisionWith(Screening other) {
        var hasSameRoom = other.hasRoom(this.room);
        var isTimeCollision = other.startBefore(this.finishDate()) && other.endAfter(this.date);
        return hasSameRoom && isTimeCollision;
    }

    public int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), date)
                .abs()
                .toHours();
    }

    private boolean hasRoom(Room room) {
        return this.room.equals(room);
    }

    private boolean startBefore(LocalDateTime date) {
        return this.date.isBefore(date);
    }

    private boolean endAfter(LocalDateTime date) {
        return this.finishDate().isAfter(date);
    }

    private LocalDateTime finishDate() {
        return date.plusMinutes(film.getDurationInMinutes());
    }
}
