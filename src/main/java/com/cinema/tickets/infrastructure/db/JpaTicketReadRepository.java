package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.TicketDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface JpaTicketReadRepository extends Repository<JpaTicket, Long> {

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            f.title,
            t.screening.date,
            t.screening.hallId,
            t.seat.rowNumber,
            t.seat.number,
            t.userId) from JpaTicket t
            left join JpaFilm f on f.id = t.screening.filmId
            where t.screening.id = :id
            """)
    List<TicketDto> getByScreeningId(long id);

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            f.title,
            t.screening.date,
            t.screening.hallId,
            t.seat.rowNumber,
            t.seat.number,
            t.userId) from JpaTicket t
            left join JpaFilm f on f.id = t.screening.filmId
            where t.userId = :id
            """)
    List<TicketDto> getByUserId(long id);
}

