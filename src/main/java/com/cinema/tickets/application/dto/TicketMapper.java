package com.cinema.tickets.application.dto;

import com.cinema.tickets.domain.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketMapper {

    @Mapping(target = "filmTitle", source = "ticket.filmTitle")
    @Mapping(target = "screeningDate", source = "ticket.screeningDate")
    @Mapping(target = "roomCustomId", source = "ticket.roomCustomId")
    @Mapping(target = "seatRowNumber", source = "ticket.rowNumber")
    @Mapping(target = "seatNumber", source = "ticket.seatNumber")
    TicketDto mapToDto(Ticket ticket);
}
