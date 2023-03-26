package code.bookings.domain;

import code.bookings.application.dto.RoomDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Room {

    @Id
    private UUID id;

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    public RoomDto toDto() {
        return new RoomDto(
                id,
                number,
                rowsQuantity,
                seatsInOneRowQuantity,
                seatsInOneRowQuantity * rowsQuantity
        );
    }
}
