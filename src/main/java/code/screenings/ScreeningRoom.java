package code.screenings;

import code.screenings.dto.ScreeningRoomDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningRoom {

    @Id
    private UUID id;

    private int number;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    @Getter
    private List<ScreeningRoomSeat> seats;

    Optional<ScreeningRoomSeat> getSeat(UUID seatId) {
        return seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    int freeSeatsQuantity() {
        return (int) this.seats
                .stream()
                .filter(ScreeningRoomSeat::isFree)
                .count();
    }

    void assignScreeningForSeats(Screening screening) {
        this
                .seats
                .forEach(seat -> seat.assignScreening(screening));
    }

    ScreeningRoomDto toDTO() {
        return new ScreeningRoomDto(
                this.id,
                this.number,
                this.rowsQuantity,
                this.seatsInOneRowQuantity,
                this.seatsInOneRowQuantity * this.rowsQuantity
        );
    }
}
