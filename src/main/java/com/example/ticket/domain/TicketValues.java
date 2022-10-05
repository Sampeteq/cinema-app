package com.example.ticket.domain;


import java.math.BigDecimal;

abstract class TicketValues {

    private TicketValues() {
    }

    static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal("10.0");

    static final BigDecimal CHILDREN_DISCOUNT_PERCENT = new BigDecimal("0.1");
}
