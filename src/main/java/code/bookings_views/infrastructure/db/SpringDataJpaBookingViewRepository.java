package code.bookings_views.infrastructure.db;

import code.bookings_views.domain.BookingView;
import code.bookings_views.domain.ports.BookingViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaBookingViewRepository implements BookingViewRepository {

    private final JpaBookingViewRepository jpaBookingViewRepository;

    @Override
    public BookingView add(BookingView bookingView) {
        return jpaBookingViewRepository.save(bookingView);
    }

    @Override
    public Optional<BookingView> readById(Long id) {
        return jpaBookingViewRepository.findById(id);
    }

    @Override
    public Optional<BookingView> readByIdAndUserId(Long id, Long userId) {
        return jpaBookingViewRepository.readByIdAndUserId(id, userId);
    }

    @Override
    public List<BookingView> readAllByUserId(Long userId) {
        return jpaBookingViewRepository.readAllByUserId(userId);
    }
}

interface JpaBookingViewRepository extends JpaRepository<BookingView, Long> {
    Optional<BookingView> readByIdAndUserId(Long id, Long userId);
    List<BookingView> readAllByUserId(Long userId);
}
