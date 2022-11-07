package code.screenings;

import code.screenings.dto.ScreeningBookingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    @Query("select new code.screenings.dto.ScreeningBookingData(" +
            "s.date.value, " +
            "s.freeSeatsQuantity) from Screening s where s.id = :screeningId")
    Optional<ScreeningBookingData> findByIdAsReservationData(@Param("screeningId") UUID screeningId);

    boolean existsByDateAndRoom_id(ScreeningDate screeningDate, UUID roomId);
}
