package com.cinema.screenings.application;

import com.cinema.tickets.domain.TicketCancelled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
class TicketCancelledHandler {

    private final JdbcTemplate jdbcTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(TicketCancelled event) {
        log.info("Handling event:{}", event);
        jdbcTemplate.update("update screenings_seats set is_free = true where id = ?", event.seatId());
    }
}
