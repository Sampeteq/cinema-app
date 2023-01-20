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

    private LocalDateTime finishDate;

    private int minAge;

    private UUID filmId;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScreeningRoom room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    static Screening of(LocalDateTime date, int minAge, UUID filmId, int filmDuration, ScreeningRoom room) {
        var screening = new Screening(
                UUID.randomUUID(),
                date,
                date.plusMinutes(filmDuration),
                minAge,
                filmId,
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

    ScreeningDto toDto() {
        return new ScreeningDto(
                this.id,
                this.date,
                this.minAge,
                this.filmId,
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
