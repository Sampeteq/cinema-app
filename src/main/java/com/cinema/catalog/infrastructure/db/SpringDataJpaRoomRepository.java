package com.cinema.catalog.infrastructure.db;

import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public Optional<Room> readFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return jpaRoomRepository
                .readFirstAvailableRoom(start, end)
                .stream()
                .findFirst();
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
    List<Room> readFirstAvailableRoom(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    boolean existsByCustomId(String customId);
}
