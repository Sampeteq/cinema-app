package code.bookings.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> getByIdAndUsername(UUID ticketId, String username);

    @Query("SELECT DISTINCT s FROM Booking s where s.username = :username")
    List<Booking> getByUsername(String username);
}
