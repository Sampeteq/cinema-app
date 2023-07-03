package code.catalog.infrastructure.db;

import code.catalog.domain.Screening;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScreeningReadOnlyRepository extends Repository<Screening, Long> {
    Optional<Screening> readById(Long id);

    @Query("select s from Screening s join fetch s.seats se where se.id = :seatId")
    Optional<Screening> readBySeatId(@Param("seatId") Long seatId);

    @Query("select s from Screening s where s.endDate < CURRENT_DATE")
    List<Screening> readEnded();
}
