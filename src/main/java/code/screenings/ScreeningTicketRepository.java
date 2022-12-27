package code.screenings;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

sealed interface ScreeningTicketRepository permits JpaScreeningTicketRepositoryAdapter {

    ScreeningTicket add(ScreeningTicket ticket);

    Optional<ScreeningTicket> getById(UUID ticketId);

    List<ScreeningTicket> getAll();
}

interface JpaScreeningTicketRepository extends JpaRepository<ScreeningTicket, UUID> {

}

@AllArgsConstructor
final class JpaScreeningTicketRepositoryAdapter implements ScreeningTicketRepository {

    private final JpaScreeningTicketRepository jpaScreeningTicketRepository;

    @Override
    public ScreeningTicket add(ScreeningTicket ticket) {
        return jpaScreeningTicketRepository.save(ticket);
    }

    @Override
    public Optional<ScreeningTicket> getById(UUID ticketId) {
        return jpaScreeningTicketRepository.findById(ticketId);
    }

    @Override
    public List<ScreeningTicket> getAll() {
        return jpaScreeningTicketRepository.findAll();
    }
}
