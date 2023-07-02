package code.catalog.application.services;

import code.catalog.domain.Room;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.ports.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoomAvailableService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public Room getFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room
                        .getScreenings()
                        .stream()
                        .noneMatch(screening -> screening.collide(start, end))
                )
                .findFirst()
                .orElseThrow(RoomsNoAvailableException::new);
    }
}
