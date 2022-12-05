package code.screenings;

import code.screenings.dto.ScreeningDto;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    private List<ScreeningSeat> seats = new ArrayList<>();

    int timeToScreeningStartInHours(Clock clock) {
        return this.date.timeToScreeningStart(clock);
    }

    Optional<ScreeningSeat> getSeat(UUID seatId) {
        return this
                .seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    ScreeningDto toDTO() {
        return ScreeningDto
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats((int) seats.stream().filter(ScreeningSeat::isFree).count())
                .minAge(this.minAge)
                .filmId(this.filmId)
                .roomId(this.room.toDTO().id())
                .seats(seats.stream().map(ScreeningSeat::toDTO).toList())
                .build();
    }
}
