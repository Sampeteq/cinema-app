package code.screening;

import code.screening.dto.ScreeningRoomDTO;
import code.screening.exception.ScreeningFreeSeatsException;
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
    @AttributeOverride(name = "value", column = @Column(name = "free_seats"))
    private ScreeningRoomFreeSeats freeSeats;

    protected ScreeningRoom() {
    }

    ScreeningRoom(int number, ScreeningRoomFreeSeats freeSeats) {
        this.number = number;
        this.freeSeats = freeSeats;
    }

    void decreaseFreeSeatsByOne(Long screeningId) {
        if (!this.freeSeats.anyFree()) {
            throw ScreeningFreeSeatsException.noFreeSeats(screeningId);
        } else {
            this.freeSeats = ScreeningRoomFreeSeats.of(this.freeSeats.getValue() - 1);
        }
    }

    int currentFree() {
        return this.freeSeats.currentFree();
    }

    ScreeningRoomDTO toDTO() {
        return new ScreeningRoomDTO(this.uuid, number, freeSeats.getValue());
    }
}
