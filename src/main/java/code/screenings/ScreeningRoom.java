package code.screenings;

import code.screenings.dto.ScreeningRoomDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "SCREENING_ROOMS")
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningRoom {

    @Id
    @Getter
    private UUID id = UUID.randomUUID();

    private int number;

    @Getter
    private int freeSeats;

    protected ScreeningRoom() {
    }

    ScreeningRoom(int number, int freeSeats) {
        this.number = number;
        this.freeSeats = freeSeats;
    }

    ScreeningRoomDTO toDTO() {
        return new ScreeningRoomDTO(this.id, number, freeSeats);
    }
}
