package com.example.ticket.domain;

import com.example.screening.domain.ScreeningAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TicketConfig {

    @Bean
    TicketAPI ticketAPI(TicketRepository ticketRepository, ScreeningAPI screeningAPI) {
        var nonAdultPercentDiscount = TicketValues.UNDERAGE_DISCOUNT_PERCENT;
        var nonAdultTicketDiscountPolicy = new PercentUnderageTicketDiscountPolicy(nonAdultPercentDiscount);
        var ticketFactory = new TicketFactory(nonAdultTicketDiscountPolicy);
        return new TicketAPI(ticketRepository, ticketFactory, screeningAPI);
    }

    @Bean
    UnderageTicketDiscountPolicy underageTicketDiscountPolicy(Double discountPercent) {
        return new PercentUnderageTicketDiscountPolicy(discountPercent);
    }

    @Bean
    Double underAgeDiscountPercent() {
        return TicketValues.UNDERAGE_DISCOUNT_PERCENT;
    }
}
