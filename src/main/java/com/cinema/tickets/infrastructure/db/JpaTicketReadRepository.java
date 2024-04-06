package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.TicketDto;
import com.cinema.tickets.domain.TicketReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface JpaTicketReadRepository extends Repository<JpaTicket, Integer> {

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            t.screening.film.title,
            t.screening.date,
            t.screening.hall.id,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from JpaTicket t where t.screening.id = :id
            """)
    List<TicketDto> getByScreeningId(Long id);

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            t.screening.film.title,
            t.screening.date,
            t.screening.hall.id,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from JpaTicket t where t.user.id = :id
            """)
    List<TicketDto> getByUserId(Long id);
}

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
class JpaTicketReadRepositoryAdapter implements TicketReadRepository {

    private final JpaTicketReadRepository jpaTicketReadRepository;

    @Override
    public List<TicketDto> getByScreeningId(Long id) {
        return jpaTicketReadRepository.getByScreeningId(id);
    }

    @Override
    public List<TicketDto> getByUserId(Long id) {
        return jpaTicketReadRepository.getByUserId(id);
    }
}
