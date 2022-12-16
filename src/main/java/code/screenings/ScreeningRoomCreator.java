package code.screenings;

import code.screenings.dto.AddScreeningRoomDto;
import code.screenings.dto.ScreeningRoomDto;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
class ScreeningRoomCreator {

    private final ScreeningRoomRepository screeningRoomRepository;

    ScreeningRoomDto addRoom(AddScreeningRoomDto dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomAlreadyExistsException(dto.number());
        }
        var seats = new ArrayList<ScreeningRoomSeat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= dto.seatsQuantityInOneRow() * dto.rowsQuantity(); i++) {
            if (helpCounter > dto.seatsQuantityInOneRow()) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = ScreeningRoomSeat
                    .builder()
                    .id(UUID.randomUUID())
                    .rowNumber(rowNumber)
                    .number(seatNumber++)
                    .status(ScreeningSeatStatus.FREE)
                    .build();
            seats.add(seat);
            helpCounter++;
        }
        var screeningRoom = ScreeningRoom
                .builder()
                .id(UUID.randomUUID())
                .number(dto.number())
                .rowsQuantity(dto.rowsQuantity())
                .seatsInOneRowQuantity(dto.seatsQuantityInOneRow())
                .seats(seats)
                .build();
        return screeningRoomRepository
                .add(screeningRoom)
                .toDTO();
    }
}
