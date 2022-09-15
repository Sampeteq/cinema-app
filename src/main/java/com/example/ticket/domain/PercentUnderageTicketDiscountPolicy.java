package com.example.ticket.domain;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
class PercentUnderageTicketDiscountPolicy implements UnderageTicketDiscountPolicy {

    private final Double discountPercent;

    @Override
    public Money calculate(Money basicPrize) {
        return basicPrize.subtractPercent(discountPercent);
    }
}
