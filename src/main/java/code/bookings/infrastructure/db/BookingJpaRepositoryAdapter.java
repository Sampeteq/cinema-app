package code.bookings.infrastructure.db;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.ports.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingJpaRepositoryAdapter implements BookingRepository {

    private final BookingJpaRepository bookingJpaRepository;

    @Override
    public Booking add(Booking booking) {
        return bookingJpaRepository.save(booking);
    }


    @Override
    public Optional<Booking> readByIdAndUserId(Long bookingId, Long userId) {
        return bookingJpaRepository.readByIdAndUserId(bookingId, userId);
    }

    @Override
    public List<Booking> readAllByUserId(Long userId) {
        return bookingJpaRepository.readAllByUserId(userId);
    }

    @Override
    public boolean existsBySeatIdAndBookingStatus(Long seatId, BookingStatus status) {
        return bookingJpaRepository.existsBySeatIdAndStatus(seatId, status);
    }
}

interface BookingJpaRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.seat " +
            "JOIN FETCH b.bookingDetails " +
            "WHERE b.id = :bookingId and b.userId = :userId")
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);

    @Query("SELECT DISTINCT b FROM Booking b " +
            "JOIN FETCH b.seat " +
            "JOIN FETCH b.bookingDetails " +
            "WHERE b.userId = :userId")
    List<Booking> readAllByUserId(@Param("userId") Long userId);

    boolean existsBySeatIdAndStatus(Long seatId, BookingStatus status);
}
