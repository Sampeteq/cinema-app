package code.bookings.infrastructure.db;

import code.bookings.domain.Seat;
import code.bookings.domain.ports.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaSeatRepository implements SeatRepository {

    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public Optional<Seat> readById(Long id) {
        return jpaSeatRepository.readByIdWithScreening(id);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {

    @Query("select s from Seat s left join fetch s.screening where s.id = :id")
    Optional<Seat> readByIdWithScreening(@Param("id") Long id);
}
