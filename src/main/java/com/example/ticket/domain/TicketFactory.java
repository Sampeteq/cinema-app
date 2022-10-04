package com.example.ticket.domain;

import com.example.ticket.domain.dto.ReserveTicketDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class TicketFactory {

    private final UnderageTicketDiscountPolicy underageTicketDiscountPolicy;

    Ticket createTicket(ReserveTicketDTO dto) {
        var ticket = new Ticket(dto.firstName(), dto.lastName(), dto.screeningId());
        if (dto.age() < 18) {
            ticket.applyDiscount(underageTicketDiscountPolicy);
        }
        return ticket;
    }
}
