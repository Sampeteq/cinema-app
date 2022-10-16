package code.screening;

import code.screening.dto.ScreeningDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByFilmId(Long filmId);

    List<Screening> findByDate(ScreeningDate date);

    @Query("select new code.screening.dto.ScreeningDTO(" +
            "s.id, " +
            "s.date.value, " +
            "s.room.freeSeats.value, " +
            "s.minAge.value, " +
            "s.filmId) from Screening s")
    List<ScreeningDTO> findAllAsDTO();
}
