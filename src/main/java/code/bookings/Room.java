package code.bookings;

import code.bookings.dto.RoomDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    List<Seat> createSeats(Screening screening) {
        var seats = new ArrayList<Seat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= seatsInOneRowQuantity * rowsQuantity; i++) {
            if (helpCounter > seatsInOneRowQuantity) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = new Seat(
                    UUID.randomUUID(),
                    rowNumber,
                    seatNumber++,
                    SeatStatus.FREE,
                    screening
            );
            seats.add(seat);
            helpCounter++;
        }
        return seats;
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
