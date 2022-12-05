package code.screenings;

import code.screenings.dto.ScreeningRoomDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningRoom {

    @Id
    private UUID id;

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    int seatsQuantity() {
        return rowsQuantity * seatsInOneRowQuantity;
    }

    ScreeningRoomDto toDTO() {
        return ScreeningRoomDto
                .builder()
                .id(this.id)
                .number(this.number)
                .rowsQuantity(this.rowsQuantity)
                .seatsInOneRowQuantity(this.seatsInOneRowQuantity)
                .seatsQuantity(this.rowsQuantity * this.seatsInOneRowQuantity)
                .build();
    }
}
