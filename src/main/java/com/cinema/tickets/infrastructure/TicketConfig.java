package com.cinema.tickets.infrastructure;

import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.domain.TicketBookingPolicy;
import com.cinema.tickets.domain.TicketCancellingPolicy;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class TicketConfig {

    @Bean
    TicketService ticketService(
            TicketRepository ticketRepository,
            ScreeningService screeningService,
            Clock clock
    ) {
        var ticketBookingPolicy = new TicketBookingPolicy();
        var ticketCancellingPolicy = new TicketCancellingPolicy();
        return new TicketService(
                ticketRepository,
                ticketBookingPolicy,
                ticketCancellingPolicy,
                screeningService,
                clock
        );
    }

    @Bean
    TicketRepository ticketRepository(TicketJpaRepository ticketJpaRepository) {
        return new TicketJpaRepositoryAdapter(ticketJpaRepository);
    }
}
