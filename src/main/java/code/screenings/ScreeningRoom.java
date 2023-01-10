package code.screenings;

import code.screenings.dto.ScreeningRoomView;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
class ScreeningRoom {

    @Id
    private UUID id;

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    int seatsQuantity() {
        return this.rowsQuantity * seatsInOneRowQuantity;
    }

    ScreeningRoomView toView() {
        return new ScreeningRoomView(
                this.id,
                this.number,
                this.rowsQuantity,
                this.seatsInOneRowQuantity,
                this.seatsInOneRowQuantity * this.rowsQuantity
        );
    }
}
