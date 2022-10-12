package code.ticket;


import java.math.BigDecimal;

abstract class TicketValues {

    private TicketValues() {
    }

    static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal("10.0");
}
