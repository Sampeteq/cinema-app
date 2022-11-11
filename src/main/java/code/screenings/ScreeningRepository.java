package code.screenings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, UUID> {



    boolean existsByDateAndRoom_id(ScreeningDate screeningDate, UUID roomId);
}
