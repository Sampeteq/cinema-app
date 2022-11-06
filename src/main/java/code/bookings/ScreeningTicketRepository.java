package code.bookings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ScreeningTicketRepository extends JpaRepository<ScreeningTicket, Long> {

    Optional<ScreeningTicket> findByUuid(UUID ticketId);
}
