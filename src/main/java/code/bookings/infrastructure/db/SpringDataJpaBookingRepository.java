package code.bookings.infrastructure.db;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaBookingRepository implements BookingRepository {

    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public Booking add(Booking booking) {
        return jpaBookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> readById(UUID bookingId) {
        return jpaBookingRepository.findById(bookingId);
    }

    @Override
    public Optional<Booking> readByIdAndUsername(UUID ticketId, String username) {
        return jpaBookingRepository.readByIdAndUsername(ticketId, username);
    }

    @Override
    public List<Booking> readByUsername(String username) {
        return jpaBookingRepository.readByUsername(username);
    }
}

interface JpaBookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> readByIdAndUsername(UUID bookingId, String username);

    @Query("SELECT DISTINCT s FROM Booking s where s.username = :username")
    List<Booking> readByUsername(String username);
}
