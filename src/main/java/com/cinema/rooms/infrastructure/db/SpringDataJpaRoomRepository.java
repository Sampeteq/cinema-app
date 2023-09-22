package com.cinema.rooms.infrastructure.db;

import com.cinema.rooms.domain.Room;
import com.cinema.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaRoomRepository implements RoomRepository {

    private final JpaRoomRepository jpaRoomRepository;

    @Override
    public Room add(Room room) {
        return jpaRoomRepository.save(room);
    }

    @Override
    public Optional<Room> readByCustomId(String customId) {
        return jpaRoomRepository.readByCustomId(customId);
    }

    @Override
    public List<Room> readAll() {
        return jpaRoomRepository.findAll();
    }

    @Override
    public Long count() {
        return jpaRoomRepository.count();
    }

    @Override
    public boolean existsByCustomId(String customId) {
        return jpaRoomRepository.existsByCustomId(customId);
    }
}

interface JpaRoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> readByCustomId(String customId);

    boolean existsByCustomId(String customId);
}
