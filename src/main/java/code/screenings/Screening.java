package code.screenings;

import code.screenings.dto.ScreeningDTO;
import code.screenings.exception.ScreeningFreeSeatsQuantityBiggerThanRoomOneException;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @Id
    @Getter
    private UUID id = UUID.randomUUID();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    private Integer minAge;

    private Integer freeSeatsQuantity;

    private UUID filmId;

    @ManyToOne
    private ScreeningRoom room;

    protected Screening() {
    }

    Screening(ScreeningDate date, int minAge, int freeSeatsQuantity, UUID filmId, ScreeningRoom room) {
        if (freeSeatsQuantity > room.getFreeSeats()) {
            throw new ScreeningFreeSeatsQuantityBiggerThanRoomOneException();
        }
        this.date = date;
        this.minAge = minAge;
        this.freeSeatsQuantity = freeSeatsQuantity;
        this.filmId = filmId;
        this.room = room;
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
                .roomUuid(this.room.getId())
                .build();
    }
}
