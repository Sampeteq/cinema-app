package com.example.ticket.domain;

import com.example.ticket.domain.dto.ReserveTicketDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class TicketFactory {

    private final UnderageTicketDiscountPolicy underageTicketDiscountPolicy;

    Ticket createTicket(ReserveTicketDTO cmd) {
        var ticket = new Ticket(cmd.firstName(), cmd.lastName(), cmd.screeningId());
        if (cmd.age() < 18) {
            ticket.applyDiscount(underageTicketDiscountPolicy);
        }
        return ticket;
    }
}
