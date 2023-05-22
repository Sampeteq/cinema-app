package code.bookings.infrastructure.db;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaBookingRepository implements BookingRepository {

    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public Booking add(Booking booking) {
        return jpaBookingRepository.save(booking);
    }

    @Override
    public List<Booking> addMany(List<Booking> bookings) {
        return jpaBookingRepository.saveAll(bookings);
    }

    @Override
    public Optional<Booking> readById(Long bookingId) {
        return jpaBookingRepository.findById(bookingId);
    }

    @Override
    public Optional<Booking> readByIdAndUserId(Long bookingId, Long userId) {
        return jpaBookingRepository.readByIdAndUserId(bookingId, userId);
    }

    @Override
    public List<Booking> readByUserId(Long userId) {
        return jpaBookingRepository.readByUserId(userId);
    }
}

interface JpaBookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);

    @Query("SELECT DISTINCT b FROM Booking b where b.userId = :userId")
    List<Booking> readByUserId(Long userId);
}
