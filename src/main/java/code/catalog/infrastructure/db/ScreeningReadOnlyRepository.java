package code.catalog.infrastructure.db;

import code.catalog.application.dto.BookingDataDto;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningReadOnlyRepository extends Repository<Screening, Long> {
    Optional<Screening> findById(Long id);

    @Query("select s from Screening s join fetch s.seats se where se.id = :seatId")
    Optional<Screening> findBySeatId(@Param("seatId") Long seatId);

    List<Screening> findAll();

    List<Screening> findByFilm_Title(String filmTitle);

    List<Screening> findByFilm_Category(FilmCategory filmCategory);

    List<Screening> findByDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Screening s where s.endDate < CURRENT_DATE")
    List<Screening> findEnded();

    @Query(
            "select new code.catalog.application.dto.BookingDataDto(" +
                    "sc.id, " +
                    "sc.date, " +
                    "r.id, " +
                    "r.customId, " +
                    "se.rowNumber, " +
                    "se.number, " +
                    "film.id, " +
                    "film.title" +
                    ") from Screening sc, Room r, Seat se, Film film where se.id = :seatId"
    )
    Page<BookingDataDto> readBookingDataBySeatId(Long seatId, Pageable pageable);
}
