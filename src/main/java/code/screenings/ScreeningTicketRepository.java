package code.screenings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ScreeningTicketRepository extends JpaRepository<ScreeningTicket, Long> {

    Optional<ScreeningTicket> findById(UUID ticketId);
}
