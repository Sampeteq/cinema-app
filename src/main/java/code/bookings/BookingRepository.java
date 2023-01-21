package code.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByIdAndUsername(UUID ticketId, String username);

    @Query("SELECT DISTINCT s FROM Booking s where s.username = :username")
    List<Booking> findByUsername(String username);
}
