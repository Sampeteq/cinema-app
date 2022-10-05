package com.example.ticket.domain;

import com.example.ticket.domain.dto.ReserveTicketDTO;
import lombok.AllArgsConstructor;

import static com.example.ticket.domain.TicketValues.CHILDREN_DISCOUNT_PERCENT;

@AllArgsConstructor
class TicketFactory {

    Ticket createTicket(ReserveTicketDTO dto) {
        var ticket = new Ticket(dto.firstName(), dto.lastName(), dto.screeningId());
        if (dto.age() < 18) {
            ticket.applyDiscount(basicPrize -> basicPrize.subtractPercent(CHILDREN_DISCOUNT_PERCENT));
        }
        return ticket;
    }
}
