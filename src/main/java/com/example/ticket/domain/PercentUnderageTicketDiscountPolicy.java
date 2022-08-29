package com.example.ticket.domain;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
class PercentUnderageTicketDiscountPolicy implements UnderageTicketDiscountPolicy {

    private final Double discountPercent;
    @Override
    public BigDecimal calculatePrize(BigDecimal basicPrize) {
        return basicPrize.subtract(basicPrize.multiply(BigDecimal.valueOf(discountPercent) ) );
    }
}
