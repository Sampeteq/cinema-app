package com.cinema.tickets.ui;

import com.cinema.users.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Executors;

class TicketConcurrencyTest extends TicketBaseIT {

    @Test
    void ticket_is_booked_only_by_one_user() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var ticket = addTicket(screening);
        var seat = ticket.getSeat();
        var users = addUsers();
        var bookTicketDto = new TicketBookRequest(screening.getId(), List.of(seat));

        //when
        try (var executorService = Executors.newFixedThreadPool(3)) {
            executorService.submit(() -> bookTicket(users.getFirst().getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(1).getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(2).getMail(), bookTicketDto));
            executorService.shutdown();
        }

        //then
        Assertions.assertThat(ticketRepository.findAll()).hasSize(1);
    }

    private void bookTicket(String userMail, TicketBookRequest ticketBookRequest) {
        webTestClient
                .post()
                .uri("/tickets/book")
                .headers(headers -> headers.setBasicAuth(userMail, UserFixture.PASSWORD))
                .bodyValue(ticketBookRequest)
                .exchange();
    }
}
