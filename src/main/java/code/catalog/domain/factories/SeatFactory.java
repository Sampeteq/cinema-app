package code.catalog.domain.factories;

import code.catalog.domain.Room;
import code.catalog.domain.Seat;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Component
public class SeatFactory {

    public List<Seat> createSeats(Room room) {
        return rangeClosed(1, room.getRowsQuantity())
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, room.getSeatsInOneRowQuantity())
                        .mapToObj(seatNumber -> Seat.of(rowNumber, seatNumber))
                ).toList();
    }
}
