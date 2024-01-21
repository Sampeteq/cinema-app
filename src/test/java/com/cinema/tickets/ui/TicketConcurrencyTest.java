package com.cinema.tickets.ui;

import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.users.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.concurrent.Executors;

class TicketConcurrencyTest extends TicketBaseIT {

    @Test
    void ticket_is_booked_only_by_one_user() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTicket(hall, film);
        var seat = screening.getTickets().getFirst().getSeat();
        var users = addUsers();
        var bookTicketDto = new BookTicketDto(screening.getId(), List.of(seat.getId()));

        //when
        try (var executorService = Executors.newFixedThreadPool(3)) {
            executorService.submit(() -> bookTicket(users.getFirst().getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(1).getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(2).getMail(), bookTicketDto));
            executorService.shutdown();
        }

        //then
        Assertions.assertThat(ticketRepository.getAll()).hasSize(1);
    }

    private void bookTicket(String userMail, BookTicketDto bookTicketDto) {
        webTestClient
                .post()
                .uri("/tickets")
                .headers(headers -> headers.setBasicAuth(userMail, UserFixture.PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .exchange();
    }
}
