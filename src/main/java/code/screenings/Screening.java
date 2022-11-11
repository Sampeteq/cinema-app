package code.screenings;

import code.screenings.dto.ScreeningDTO;
import code.screenings.exception.ScreeningFreeSeatsQuantityBiggerThanRoomOneException;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
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

    static Screening of(
            ScreeningDate date,
            int minAge,
            int freeSeatsQuantity,
            UUID filmId,
            ScreeningRoom room
    ) {
        if (freeSeatsQuantity > room.getFreeSeats()) {
            throw new ScreeningFreeSeatsQuantityBiggerThanRoomOneException();
        }
        return new Screening(
                UUID.randomUUID(),
                date,
                minAge,
                freeSeatsQuantity,
                filmId,
                room
        );
    }

    int differenceBetweenCurrentDateAndScreeningOneInHours(Clock clock) {
        return this.date.differenceBetweenCurrentDateAndScreeningOneInHours(clock);
    }

    boolean hasFreeSeats() {
        return this.freeSeatsQuantity > 0;
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

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.freeSeatsQuantity)
                .minAge(this.minAge)
                .filmId(this.filmId)
                .roomId(this.room.toDTO().id())
                .build();
    }
}
