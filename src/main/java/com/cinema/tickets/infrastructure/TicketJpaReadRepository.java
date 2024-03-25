package com.cinema.tickets.infrastructure;

import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface TicketJpaReadRepository extends Repository<Ticket, Long> {

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            t.screening.film.title,
            t.screening.date,
            t.screening.hall.id,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from Ticket t where t.user.id = :id
            """)
    List<TicketDto> getByUserId(Long id);

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            t.screening.film.title,
            t.screening.date,
            t.screening.hall.id,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from Ticket t where t.screening.id = :id
            """)
    List<TicketDto> getByScreeningId(Long id);
}
