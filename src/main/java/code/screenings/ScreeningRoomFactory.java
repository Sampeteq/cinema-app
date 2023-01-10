package code.screenings;

import code.screenings.dto.ScreeningRoomCreatingRequest;
import code.screenings.dto.ScreeningRoomView;
import code.screenings.exception.ScreeningRoomException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
class ScreeningRoomFactory {

    private final ScreeningRoomRepository screeningRoomRepository;

    ScreeningRoomView createRoom(ScreeningRoomCreatingRequest dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomException("Screening room already exists: " + dto.number());
        }
        var screeningRoom = new ScreeningRoom(
                UUID.randomUUID(),
                dto.number(),
                dto.rowsQuantity(),
                dto.seatsQuantityInOneRow()
        );
        return screeningRoomRepository
                .save(screeningRoom)
                .toView();
    }
}
