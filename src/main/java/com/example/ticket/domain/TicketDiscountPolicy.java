package com.example.ticket.domain;

interface TicketDiscountPolicy {

    Money calculate(Money basicPrize);
}
