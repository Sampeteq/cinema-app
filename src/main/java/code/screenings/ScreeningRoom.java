package code.screenings;

import code.screenings.dto.ScreeningRoomDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "SCREENING_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningRoom {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private int number;

    @Getter
    private int freeSeats;

    ScreeningRoomDTO toDTO() {
        return new ScreeningRoomDTO(this.id, number, freeSeats);
    }
}
