package code.screenings;

import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningReservationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByFilmId(Long filmId);

    List<Screening> findByDate(ScreeningDate date);

    @Query("select new code.screenings.dto.ScreeningDTO(" +
            "s.id, " +
            "s.date.value, " +
            "s.room.freeSeats, " +
            "s.minAge, " +
            "s.filmId) from Screening s")
    List<ScreeningDTO> findAllAsDTO();

    @Query("select new code.screenings.dto.ScreeningReservationData(" +
            "s.date.value, " +
            "s.freeSeatsQuantity) from Screening s where s.id = :screeningId")
    Optional<ScreeningReservationData> findByIdAsReservationData(@Param("screeningId") Long screeningId);

    boolean existsByDateAndRoom_uuid(ScreeningDate screeningDate, UUID roomUuid);
}
