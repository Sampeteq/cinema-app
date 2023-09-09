package code.bookings.infrastructure.db;

import code.bookings.domain.Booking;
import code.bookings.domain.ports.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaBookingRepository implements BookingRepository {

    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public Booking add(Booking booking) {
        return jpaBookingRepository.save(booking);
    }


    @Override
    public Optional<Booking> readByIdAndUserId(Long bookingId, Long userId) {
        return jpaBookingRepository.readByIdAndUserId(bookingId, userId);
    }
}

interface JpaBookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.seat s " +
            "LEFT JOIN FETCH s.screening " +
            "WHERE b.id = :bookingId and b.userId = :userId")
    Optional<Booking> readByIdAndUserId(@Param("bookingId") Long bookingId, @Param("userId") Long userId);
}
