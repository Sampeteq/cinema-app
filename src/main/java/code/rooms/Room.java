package code.rooms;

import code.rooms.dto.RoomDetails;
import code.rooms.dto.RoomDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Room {

    @Id
    private UUID id;

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    RoomDetails toDetails() {
        return new RoomDetails(
                this.rowsQuantity,
                this.seatsInOneRowQuantity
        );
    }

    RoomDto toDto() {
        return new RoomDto(
                this.id,
                this.number,
                this.rowsQuantity,
                this.seatsInOneRowQuantity,
                this.seatsInOneRowQuantity * this.rowsQuantity
        );
    }
}
