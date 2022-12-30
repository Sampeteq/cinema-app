package code.screenings;

import code.screenings.dto.ScreeningDto;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @Id
    @Getter
    private UUID id;

    private LocalDateTime date;

    private int minAge;

    private UUID filmId;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScreeningRoom room;

    Optional<Seat> getSeat(UUID seatId) {
        return room.getSeat(seatId);
    }

    int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), this.date)
                .abs()
                .toHours();
    }

    ScreeningDto toDTO() {
        return new ScreeningDto(
                this.id,
                this.date,
                this.room.freeSeatsQuantity(),
                this.minAge,
                this.filmId,
                this.room.toDTO().id(),
                this.room.getSeats().stream().map(Seat::toDTO).toList()
        );
    }
}
