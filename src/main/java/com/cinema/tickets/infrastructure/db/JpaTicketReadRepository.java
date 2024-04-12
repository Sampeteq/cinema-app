package com.cinema.tickets.infrastructure.db;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.application.dto.TicketUserDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface JpaTicketReadRepository extends Repository<JpaTicket, UUID> {

    @Query("""
            select new com.cinema.tickets.application.dto.TicketDto(
            t.seat.rowNumber,
            t.seat.number,
            case when t.userId is null then true else false end) from JpaTicket t
            where t.screening.id = :id
            """)
    List<TicketDto> getByScreeningId(UUID id);

    @Query("""
            select new com.cinema.tickets.application.dto.TicketUserDto(
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
    List<TicketUserDto> getByUserId(UUID id);
}

