package code.films;

import code.films.dto.ScreeningRoomDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class ScreeningRoomSearcher {

    private final ScreeningRoomRepository screeningRoomRepository;

    public List<ScreeningRoomDto> searchAllRooms() {
        return screeningRoomRepository
                .findAll()
                .stream()
                .map(ScreeningRoom::toDto)
                .toList();
    }
}
