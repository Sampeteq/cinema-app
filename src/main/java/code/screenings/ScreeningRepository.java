package code.screenings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    @Query("SELECT s FROM Screening s " +
            "left join fetch s.room " +
            "where " +
            "(:#{#params?.date} is null or s.date = :#{#params?.date}) and " +
            "(:#{#params.filmId} is null or s.film.id = :#{#params.filmId})")
    List<Screening> findBy(ScreeningSearchParams params);

    boolean existsByFinishDateGreaterThanAndDateLessThanAndRoomId(
            LocalDateTime date,
            LocalDateTime finishDate,
            UUID roomId
    );
}
