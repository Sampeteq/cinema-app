package com.example.ticket.domain;

import com.example.ticket.domain.dto.ReserveTicketDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
class TicketFactory {

    private final UnderageTicketDiscountPolicy underageTicketDiscountPolicy;

    Ticket createTicket(ReserveTicketDTO cmd) {
        var ticket = new Ticket(cmd.firstName(), cmd.lastName(), cmd.screeningId() );
        if (cmd.age() < 18) {
            ticket.applyDiscount(underageTicketDiscountPolicy);
        }
        return ticket;
    }
}
