package code.screenings;

import code.screenings.dto.ScreeningView;
import code.screenings.dto.SeatView;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @Id
    @Getter
    private UUID id;

    private LocalDateTime date;

    private LocalDateTime finishDate;

    private int minAge;

    @ManyToOne
    private Film film;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScreeningRoom room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    static Screening of(LocalDateTime date, int minAge, Film film, ScreeningRoom room) {
        var screening = new Screening(
                UUID.randomUUID(),
                date,
                date.plusMinutes(film.getDurationInMinutes()),
                minAge,
                film,
                room,
                new ArrayList<>()
        );
        screening.seats = room.createSeats(screening);
        return screening;
    }

    Optional<Seat> getSeat(UUID seatId) {
        return this.seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), this.date)
                .abs()
                .toHours();
    }

    ScreeningView toView() {
        return new ScreeningView(
                this.id,
                this.date,
                this.minAge,
                this.film.toView().id(),
                this.room.toView().id(),
                (int) this.seats.stream().filter(Seat::isFree).count()
        );
    }

    List<SeatView> seatsViews() {
        return this
                .seats
                .stream()
                .map(Seat::toView)
                .toList();
    }
}
