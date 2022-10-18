package code.screening;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByUuid(UUID ticketId);
}
