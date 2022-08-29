package com.example.ticket.domain;

import java.math.BigDecimal;

interface TicketDiscountPolicy {
    BigDecimal calculatePrize(BigDecimal basicPrize);
}
