package code.screenings;

import code.screenings.dto.ScreeningRoomView;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class ScreeningRoomSearcher {

    private final ScreeningRoomRepository screeningRoomRepository;

    public List<ScreeningRoomView> searchAllRooms() {
        return screeningRoomRepository
                .findAll()
                .stream()
                .map(ScreeningRoom::toView)
                .toList();
    }
}
