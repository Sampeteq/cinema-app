package com.example.ticket.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class PercentUnderageTicketDiscountPolicy implements UnderageTicketDiscountPolicy {

    private final Double discountPercent;

    @Override
    public Money calculate(Money basicPrize) {
        return basicPrize.subtractPercent(discountPercent);
    }
}
