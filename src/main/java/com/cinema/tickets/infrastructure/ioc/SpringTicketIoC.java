package com.cinema.tickets.infrastructure.ioc;

import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.ScreeningService;
import com.cinema.screenings.infrastructure.db.JpaScreeningMapper;
import com.cinema.tickets.domain.TicketReadRepository;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketService;
import com.cinema.tickets.infrastructure.db.JpaTicketMapper;
import com.cinema.tickets.infrastructure.db.JpaTicketReadRepository;
import com.cinema.tickets.infrastructure.db.JpaTicketReadRepositoryAdapter;
import com.cinema.tickets.infrastructure.db.JpaTicketRepository;
import com.cinema.tickets.infrastructure.db.JpaTicketRepositoryAdapter;
import com.cinema.users.infrastructure.db.JpaUserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class SpringTicketIoC {

    @Bean
    TicketService ticketService(
            TicketRepository ticketRepository,
            TicketReadRepository ticketReadRepository,
            ScreeningService screeningService,
            HallService hallService,
            Clock clock
    ) {
        return new TicketService(ticketRepository, ticketReadRepository, screeningService, hallService, clock);
    }

    @Bean
    TicketRepository ticketRepository(JpaTicketRepository jpaTicketRepository, JpaTicketMapper mapper) {
        return new JpaTicketRepositoryAdapter(jpaTicketRepository, mapper);
    }

    @Bean
    TicketReadRepository ticketReadRepository(JpaTicketReadRepository jpaTicketReadRepository) {
        return new JpaTicketReadRepositoryAdapter(jpaTicketReadRepository);
    }

    @Bean
    JpaTicketMapper jpaTicketMapper(JpaScreeningMapper screeningMapper, JpaUserMapper userMapper) {
        return new JpaTicketMapper(screeningMapper, userMapper);
    }
}
