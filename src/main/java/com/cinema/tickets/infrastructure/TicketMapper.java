package com.cinema.tickets.infrastructure;

import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketMapper {
    @Mapping(source = "seat.screening.film.title", target = "filmTitle")
    @Mapping(source = "seat.screening.date", target = "screeningDate")
    @Mapping(source = "seat.screening.hall.id", target = "hallId")
    @Mapping(source = "seat.hallSeat.rowNumber", target = "rowNumber")
    @Mapping(source = "seat.hallSeat.number", target = "seatNumber")
    TicketDto mapToDto(Ticket ticket);
}
