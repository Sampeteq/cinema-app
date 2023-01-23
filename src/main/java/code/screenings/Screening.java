package code.screenings;

import code.screenings.dto.ScreeningDto;
import code.screenings.dto.SeatDto;
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

    private int minAge;

    @ManyToOne
    private Film film;

    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    static Screening of(LocalDateTime date, int minAge, Film film, Room room) {
        var screening = new Screening(
                UUID.randomUUID(),
                date,
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

    boolean IsTimeCollision(LocalDateTime start, LocalDateTime finish) {
        var screeningFinishDate = this.date.plusMinutes(film.getDurationInMinutes());
        return screeningFinishDate.isAfter(start) && this.date.isBefore(finish);
    }

    ScreeningDto toDto() {
        return new ScreeningDto(
                this.id,
                this.date,
                this.minAge,
                this.film.getId(),
                this.room.toDto().id(),
                (int) this.seats.stream().filter(Seat::isFree).count()
        );
    }

    List<SeatDto> seatsDtos() {
        return this
                .seats
                .stream()
                .map(Seat::toDto)
                .toList();
    }
}
