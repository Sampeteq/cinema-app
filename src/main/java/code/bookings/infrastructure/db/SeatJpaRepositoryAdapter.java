package code.bookings.infrastructure.db;

import code.bookings.domain.Seat;
import code.bookings.domain.ports.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatJpaRepositoryAdapter implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Optional<Seat> readById(Long id) {
        return seatJpaRepository.findById(id);
    }
}

interface SeatJpaRepository extends JpaRepository<Seat, Long> {

}
