package code.screenings;

import code.screenings.dto.ScreeningDTO;
import code.screenings.exception.ScreeningNoFreeSeatsException;
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
@Builder
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

    private Integer freeSeatsQuantity;

    private UUID filmId;

    @ManyToOne
    private ScreeningRoom room;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    @Getter
    private List<ScreeningSeat> seats = new ArrayList<>();

    static Screening of(
            ScreeningDate date,
            int minAge,
            UUID filmId,
            ScreeningRoom room,
            List<ScreeningSeat> seats
    ) {
        return new Screening(
                UUID.randomUUID(),
                date,
                minAge,
                (int) seats.stream().filter(ScreeningSeat::isFree).count(),
                filmId,
                room,
                seats
        );
    }

    int differenceBetweenCurrentDateAndScreeningOneInHours(Clock clock) {
        return this.date.differenceBetweenCurrentDateAndScreeningOneInHours(clock);
    }

    boolean hasFreeSeats() {
        return this
                .seats
                .stream()
                .anyMatch(ScreeningSeat::isFree);
    }

    void decreaseFreeSeatsByOne() {
        if (this.freeSeatsQuantity < 0) {
            throw new ScreeningNoFreeSeatsException(this.id);
        } else {
            this.freeSeatsQuantity--;
        }
    }

    void increaseFreeSeatsByOne() {
        this.freeSeatsQuantity++;
    }

    Optional<ScreeningSeat> getSeat(UUID seatId) {
        return this
                .seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.freeSeatsQuantity)
                .minAge(this.minAge)
                .filmId(this.filmId)
                .roomId(this.room.toDTO().id())
                .seats(seats.stream().map(ScreeningSeat::toDTO).toList())
                .build();
    }
}
