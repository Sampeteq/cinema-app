package com.cinema.tickets_views.application.dto;

import com.cinema.tickets_views.domain.TicketView;
import org.mapstruct.Mapper;

@Mapper
public interface TicketViewMapper {
    TicketViewDto mapToDto(TicketView ticketView);
}
