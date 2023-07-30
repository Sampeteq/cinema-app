package code.bookings.infrastructure.db;

import code.bookings.domain.Seat;
import code.bookings.domain.ports.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaSeatRepository implements SeatRepository {

    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public Optional<Seat> readById(Long id) {
        return jpaSeatRepository.findById(id);
    }
}

interface JpaSeatRepository extends JpaRepository<Seat, Long> {

}
