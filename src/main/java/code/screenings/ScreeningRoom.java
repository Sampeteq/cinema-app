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
    private List<Seat> seats;

    Optional<Seat> getSeat(UUID seatId) {
        return seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    int freeSeatsQuantity() {
        return (int) this.seats
                .stream()
                .filter(Seat::isFree)
                .count();
    }

    void assignNewScreening(Screening screening) {
        if (this.seats.stream().allMatch(Seat::hasNoAssignedScreening)) {
            this.seats.forEach(seat -> seat.assignScreening(screening));
        } else {
            var newSeats = this.seats
                    .stream()
                    .map(seat -> seat.copyWithNewScreening(screening))
                    .toList();
            this.seats.addAll(newSeats);
        }
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
