package code.catalog.infrastructure.db;

import code.catalog.domain.Room;
import code.catalog.domain.ports.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    public List<Room> readAll() {
        return jpaRoomRepository.findAll();
    }

    @Override
    public boolean existsByCustomId(String customId) {
        return jpaRoomRepository.existsByCustomId(customId);
    }
}

interface RoomJpaRepository extends JpaRepository<Room, Long> {
    boolean existsByCustomId(String customId);
}
