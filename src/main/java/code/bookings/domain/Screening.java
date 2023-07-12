package code.bookings.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "booking_screening")
@Table(name = "bookings_screenings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Screening {

    @Id
    private Long id;

    private LocalDateTime date;

    @ManyToOne(cascade = CascadeType.ALL)
    private Film film;

    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private Set<Seat> seats = new HashSet<>();

    private Screening(Long id, LocalDateTime date, Film film, Room room) {
        this.id = id;
        this.date = date;
        this.film = film;
        this.room = room;
    }

    public static Screening create(Long id, LocalDateTime date, Film film, Room room) {
        return new Screening(id, date, film, room);
    }

    public int timeToScreeningInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), date)
                .abs()
                .toHours();
    }

    public void addSeat(Seat seat) {
        seat.assignScreening(this);
        this.seats.add(seat);
    }
}
