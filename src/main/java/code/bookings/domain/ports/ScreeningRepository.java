package code.bookings.domain.ports;

import code.bookings.domain.Screening;

import java.util.Optional;

public interface ScreeningRepository {
    Optional<Screening> readById(Long id);
}
