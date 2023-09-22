package com.cinema.tickets.application.dto;

import com.cinema.tickets.domain.Ticket;
import org.mapstruct.Mapper;

@Mapper
public interface TicketMapper {
    TicketDto mapToDto(Ticket ticket);
}
