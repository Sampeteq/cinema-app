package code.screenings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface SeatBookingRepository extends JpaRepository<SeatBooking, UUID> {

    Optional<SeatBooking> findByIdAndUsername(UUID ticketId, String username);
}
