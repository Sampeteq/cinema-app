package com.cinema.tickets.application.dto;

import com.cinema.tickets.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketMapper {

    @Mapping(target = "filmTitle", source = "ticket.seat.screening.filmTitle")
    @Mapping(target = "screeningDate", source = "ticket.seat.screening.date")
    @Mapping(target = "roomCustomId", source = "ticket.seat.screening.roomCustomId")
    @Mapping(target = "seatRowNumber", source = "ticket.seat.rowNumber")
    @Mapping(target = "seatNumber", source = "ticket.seat.number")
    TicketDto mapToDto(Ticket ticket);
}
