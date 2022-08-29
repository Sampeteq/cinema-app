package com.example.ticket.domain;

import java.math.BigDecimal;

abstract class TicketValues {

    private TicketValues() {}

    static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal(10);

    static final Double UNDERAGE_DISCOUNT_PERCENT = 0.1D;
}
