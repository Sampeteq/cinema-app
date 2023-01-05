package code.screenings;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

sealed interface SeatBookingRepository permits JpaSeatBookingRepositoryAdapter {

    SeatBooking add(SeatBooking ticket);

    Optional<SeatBooking> getById(UUID ticketId);

    List<SeatBooking> getAll();

    Optional<SeatBooking> getByIdAndUsername(UUID ticketId, String username);
}

interface JpaScreeningSeatBookingRepository extends JpaRepository<SeatBooking, UUID> {

    Optional<SeatBooking> getByIdAndUsername(UUID bookingId, String username);
}

@AllArgsConstructor
final class JpaSeatBookingRepositoryAdapter implements SeatBookingRepository {

    private final JpaScreeningSeatBookingRepository jpaScreeningSeatBookingRepository;

    @Override
    public SeatBooking add(SeatBooking ticket) {
        return jpaScreeningSeatBookingRepository.save(ticket);
    }

    @Override
    public Optional<SeatBooking> getById(UUID ticketId) {
        return jpaScreeningSeatBookingRepository.findById(ticketId);
    }

    @Override
    public Optional<SeatBooking> getByIdAndUsername(UUID ticketId, String username) {
        return jpaScreeningSeatBookingRepository.getByIdAndUsername(ticketId, username);
    }

    @Override
    public List<SeatBooking> getAll() {
        return jpaScreeningSeatBookingRepository.findAll();
    }
}
