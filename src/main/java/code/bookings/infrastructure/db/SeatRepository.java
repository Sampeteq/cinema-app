package code.bookings.infrastructure.db;

import code.catalog.application.dto.SeatDataDto;
import code.bookings.domain.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(
            "select new code.catalog.application.dto.SeatDataDto(" +
                    "sc.id, " +
                    "sc.date, " +
                    "r.id, " +
                    "r.customId, " +
                    "film.id, " +
                    "film.title" +
                    ") from Screening sc, Room r, Seat se, Film film where se.id = :seatId"
    )
    Page<SeatDataDto> readBookingDataBySeatId(Long seatId, Pageable pageable);
}
