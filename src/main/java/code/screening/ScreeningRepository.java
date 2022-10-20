package code.screening;

import code.screening.dto.ScreeningReservationData;
import code.screening.dto.ScreeningDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByFilmId(Long filmId);

    List<Screening> findByDate(ScreeningDate date);

    @Query("select new code.screening.dto.ScreeningDTO(" +
            "s.id, " +
            "s.date.value, " +
            "s.room.freeSeats, " +
            "s.minAge, " +
            "s.filmId) from Screening s")
    List<ScreeningDTO> findAllAsDTO();

    @Query("select new code.screening.dto.ScreeningReservationData(" +
            "s.date.value, " +
            "s.room.freeSeats) from Screening s where s.id = :screeningId")
    Optional<ScreeningReservationData> findByIdAsReservationData(@Param("screeningId") Long screeningId);
}
