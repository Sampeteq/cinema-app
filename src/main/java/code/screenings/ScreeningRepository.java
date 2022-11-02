package code.screenings;

import code.screenings.dto.ScreeningReservationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    @Query("select new code.screenings.dto.ScreeningReservationData(" +
            "s.date.value, " +
            "s.freeSeatsQuantity) from Screening s where s.id = :screeningId")
    Optional<ScreeningReservationData> findByIdAsReservationData(@Param("screeningId") Long screeningId);

    boolean existsByDateAndRoom_uuid(ScreeningDate screeningDate, UUID roomUuid);
}
