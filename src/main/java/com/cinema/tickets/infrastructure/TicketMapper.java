package com.cinema.tickets.infrastructure;

import com.cinema.tickets.ui.TicketDto;
import com.cinema.tickets.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketMapper {
    @Mapping(source = "screening.film.title", target = "filmTitle")
    @Mapping(source = "screening.date", target = "screeningDate")
    @Mapping(source = "screening.hall.id", target = "hallId")
    @Mapping(source = "seat.rowNumber", target = "rowNumber")
    @Mapping(source = "seat.number", target = "seatNumber")
    TicketDto mapToDto(Ticket ticket);
}
