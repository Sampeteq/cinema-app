package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.domain.TicketDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface JpaTicketReadRepository extends Repository<JpaTicket, Integer> {

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            f.title,
            t.screening.date,
            t.screening.hallId,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from JpaTicket t
            left join JpaFilm f on f.id = t.screening.filmId
            where t.screening.id = :id
            """)
    List<TicketDto> getByScreeningId(Long id);

    @Query("""
            select new com.cinema.tickets.domain.TicketDto(
            t.id,
            f.title,
            t.screening.date,
            t.screening.hallId,
            t.seat.rowNumber,
            t.seat.number,
            t.user.id) from JpaTicket t
            left join JpaFilm f on f.id = t.screening.filmId
            where t.user.id = :id
            """)
    List<TicketDto> getByUserId(Long id);
}

