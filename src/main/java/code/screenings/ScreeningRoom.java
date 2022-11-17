package code.screenings;

import code.screenings.dto.ScreeningRoomDTO;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
class ScreeningRoom {

    @Id
    private UUID id = UUID.randomUUID();

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    int seatsQuantity() {
        return rowsQuantity * seatsInOneRowQuantity;
    }

    ScreeningRoomDTO toDTO() {
        return ScreeningRoomDTO
                .builder()
                .id(this.id)
                .number(this.number)
                .rowsQuantity(this.rowsQuantity)
                .seatsInOneRowQuantity(this.seatsInOneRowQuantity)
                .seatsQuantity(this.rowsQuantity * this.seatsInOneRowQuantity)
                .build();
    }
}
