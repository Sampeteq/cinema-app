package code.screenings;

import code.screenings.dto.AddScreeningRoomDto;
import code.screenings.dto.ScreeningRoomDto;
import code.screenings.exception.ScreeningRoomException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
class ScreeningRoomFactory {

    private final ScreeningRoomRepository screeningRoomRepository;

    ScreeningRoomDto addRoom(AddScreeningRoomDto dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomException("Screening room already exists: " + dto.number());
        }
        var seats = new ArrayList<Seat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= dto.seatsQuantityInOneRow() * dto.rowsQuantity(); i++) {
            if (helpCounter > dto.seatsQuantityInOneRow()) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = new Seat(
                    UUID.randomUUID(),
                    rowNumber,
                    seatNumber++,
                    SeatStatus.FREE,
                    null
            );
            seats.add(seat);
            helpCounter++;
        }
        var screeningRoom = new ScreeningRoom(
                UUID.randomUUID(),
                dto.number(),
                dto.rowsQuantity(),
                dto.seatsQuantityInOneRow(),
                seats
        );
        return screeningRoomRepository
                .add(screeningRoom)
                .toDTO();
    }
}
