package code.bookings.infrastructure.db;

import code.bookings.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingFilmRepository extends JpaRepository<Film, Long> {
}
