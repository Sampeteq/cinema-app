package code.screening;

import code.screening.dto.ScreeningRoomDTO;
import code.screening.exception.NoScreeningFreeSeatsException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENING_ROOMS")
@EqualsAndHashCode(of = "uuid")
@ToString
class ScreeningRoom {

    @Id
    @Getter
    private UUID uuid = UUID.randomUUID();

    private int number;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "freeSeats"))
    @Getter
    private FreeSeats freeSeats;

    protected ScreeningRoom() {
    }

    ScreeningRoom(int number, FreeSeats freeSeats) {
        this.number = number;
        this.freeSeats = freeSeats;
    }

    void decreaseFreeSeatsByOne(Long screeningId) {
        if (this.freeSeats.getValue() == 0) {
            throw new NoScreeningFreeSeatsException(screeningId);
        } else {
            this.freeSeats = FreeSeats.of(this.freeSeats.getValue() - 1);
        }
    }

    ScreeningRoomDTO toDTO() {
        return new ScreeningRoomDTO(this.uuid, number, freeSeats.getValue());
    }
}
