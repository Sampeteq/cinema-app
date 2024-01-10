package com.cinema.tickets.infrastructure;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaTicketRepositoryAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket add(Ticket ticket) {
        return jpaTicketRepository.save(ticket);
    }


    @Override
    public Optional<Ticket> getByIdAndUserId(Long ticketId, Long userId) {
        return jpaTicketRepository.findByIdAndUserId(ticketId, userId);
    }

    @Override
    public List<TicketDto> getAllByUserId(Long userId) {
        return jpaTicketRepository.findAllByUserId(userId);
    }

    @Override
    public List<Ticket> getAll() {
        return jpaTicketRepository.findAll();
    }
}

interface JpaTicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
            from Ticket ticket
            left join fetch ticket.seat seat
            left join fetch seat.screening screening
            where ticket.id = :ticketId and ticket.user.id = :userId
            """)
    Optional<Ticket> findByIdAndUserId(@Param("ticketId") Long ticketId, @Param("userId") Long userId);

    @Query("""
            select new com.cinema.tickets.application.dto.TicketDto(
            t.id,
            t.status,
            t.seat.screening.film.title,
            t.seat.screening.date,
            t.seat.screening.hall.id,
            t.seat.hallSeat.number,
            t.seat.hallSeat.rowNumber)
            from Ticket t
            """)
    List<TicketDto> findAllByUserId(@Param("userId") Long userId);
}
