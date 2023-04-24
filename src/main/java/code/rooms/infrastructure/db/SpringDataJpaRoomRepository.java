package code.rooms.infrastructure.db;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaRoomRepository implements RoomRepository {

    private final JpaRoomRepository jpaRoomRepository;

    @Override
    public Room add(Room room) {
        return jpaRoomRepository.save(room);
    }

    @Override
    public Optional<Room> readById(UUID roomId) {
        return jpaRoomRepository.findById(roomId);
    }

    @Override
    public List<Room> readAll() {
        return jpaRoomRepository.findAll();
    }

    @Override
    public boolean existsByNumber(int number) {
        return jpaRoomRepository.existsByNumber(number);
    }
}

interface JpaRoomRepository extends JpaRepository<Room, UUID> {
    boolean existsByNumber(int number);
}