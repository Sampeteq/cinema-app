package code.films;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SeatBookingRepository extends JpaRepository<SeatBooking, UUID> {

    Optional<SeatBooking> findByIdAndUsername(UUID ticketId, String username);

    @Query("SELECT DISTINCT s FROM SeatBooking s where s.username = :username")
    List<SeatBooking> findByUsername(String username);
}
