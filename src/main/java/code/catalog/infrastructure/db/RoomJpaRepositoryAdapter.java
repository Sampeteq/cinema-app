package code.catalog.infrastructure.db;

import code.catalog.domain.Room;
import code.catalog.domain.ports.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomJpaRepositoryAdapter implements RoomRepository {

    private final RoomJpaRepository jpaRoomRepository;

    @Override
    public Room add(Room room) {
        return jpaRoomRepository.save(room);
    }

    @Override
    public Optional<Room> readById(Long roomId) {
        return jpaRoomRepository.findById(roomId);
    }

    @Override
    public Optional<Room> readFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return jpaRoomRepository
                .readFirstAvailableRoom(start, end)
                .stream()
                .findFirst();
    }

    @Override
    public List<Room> readAll() {
        return jpaRoomRepository.findAll();
    }

    @Override
    public boolean existsByCustomId(String customId) {
        return jpaRoomRepository.existsByCustomId(customId);
    }
}

interface RoomJpaRepository extends JpaRepository<Room, Long> {

    @Query(
            "SELECT r FROM Room r WHERE NOT EXISTS " +
                    "(" +
                    "SELECT s FROM Screening s WHERE " +
                    "s.room = r AND " +
                    "(" +
                    "s.date between :start AND :end OR " +
                    "s.endDate between :start and :end" +
                    ")" +
                    ")"
    )
    List<Room> readFirstAvailableRoom(LocalDateTime start, LocalDateTime end);

    boolean existsByCustomId(String customId);
}
