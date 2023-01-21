package code.films;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, UUID> {

    boolean existsByNumber(int number);
}
