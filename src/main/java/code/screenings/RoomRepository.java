package code.screenings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface RoomRepository extends JpaRepository<Room, UUID> {

    boolean existsByNumber(int number);
}
