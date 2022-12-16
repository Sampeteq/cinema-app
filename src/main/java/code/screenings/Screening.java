package code.screenings;

import code.screenings.dto.ScreeningDto;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @Id
    @Getter
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    private Integer minAge;

    private UUID filmId;

    @ManyToOne
    private ScreeningRoom room;

    Optional<ScreeningRoomSeat> getSeat(UUID seatId) {
        return room.getSeat(seatId);
    }

    int timeToScreeningStartInHours(Clock clock) {
        return this.date.timeToScreeningStart(clock);
    }

    ScreeningDto toDTO() {
        return ScreeningDto
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .minAge(this.minAge)
                .filmId(this.filmId)
                .roomId(this.room.toDTO().id())
                .freeSeats(this.room.freeSeatsQuantity())
                .seats(this.room.getSeats().stream().map(ScreeningRoomSeat::toDTO).toList())
                .build();
    }
}
