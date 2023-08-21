package code.bookings.domain.ports;

import code.bookings.domain.Screening;

import java.util.Optional;

public interface ScreeningRepository {
    Screening add(Screening screening);
    Optional<Screening> readByIdWithSeats(Long id);
    Optional<Screening> readByIdWithSeat(Long id, int rowNumber, int seatNumber);
}
