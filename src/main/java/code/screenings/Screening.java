package code.screenings;

import code.screenings.dto.ScreeningDTO;
import code.screenings.exception.ScreeningFreeSeatsQuantityBiggerThanRoomOneException;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@EqualsAndHashCode(of = "uuid")
@ToString
class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private UUID uuid = UUID.randomUUID();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    private int minAge;

    private int freeSeatsQuantity;

    private Long filmId;

    @ManyToOne
    private ScreeningRoom room;

    protected Screening() {
    }

    Screening(ScreeningDate date, int minAge, int freeSeatsQuantity, Long filmId, ScreeningRoom room) {
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
                .build();
    }
}
