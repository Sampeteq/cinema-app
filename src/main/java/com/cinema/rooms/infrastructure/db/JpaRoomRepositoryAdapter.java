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
class JpaRoomRepositoryAdapter implements RoomRepository {

    private final JpaRoomRepository jpaRoomRepository;

    @Override
    public Room add(Room room) {
        return jpaRoomRepository.save(room);
    }

    @Override
    public Optional<Room> readById(String id) {
        return jpaRoomRepository.findById(id);
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
    public boolean existsById(String id) {
        return jpaRoomRepository.existsById(id);
    }
}

interface JpaRoomRepository extends JpaRepository<Room, String> {
}
