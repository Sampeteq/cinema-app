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
import java.util.UUID;

import static java.util.stream.IntStream.rangeClosed;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "seats")
@With
public class Screening {

    @Id
    private UUID id;

    private LocalDateTime date;

    @ManyToOne
    @ToStringExclude
    private Film film;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId")
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    @ToStringExclude
    private List<Seat> seats;

    public static Screening of(LocalDateTime date, Film film, Room room) {
        var screening = new Screening(
                UUID.randomUUID(),
                date,
                film,
                room,
                new ArrayList<>()
        );
        screening.seats = createSeats(room, screening);
        return screening;
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

    private static List<Seat> createSeats(Room room, Screening screening) {
        return screening.seats = rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber, screening))
                ).toList();
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
