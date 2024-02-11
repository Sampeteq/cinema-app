package com.cinema.tickets.ui;

import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketControllerIT extends TicketBaseIT {

    private User user;

    @BeforeEach
    void setUpUser() {
        user = userRepository.save(UserFixture.createUser());
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var seatId = 1L;
        var bookTicketDto = new TicketBookRequest(
                nonExistingScreeningId,
                List.of(seatId)
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new ScreeningNotFoundException().getMessage();
        spec
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked_for_existing_seat() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var nonExistingSeatId = 0L;
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(nonExistingSeatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new ScreeningSeatNotFoundException().getMessage();
        spec
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(film, hall);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(screening.getTickets().getFirst().getSeat().getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.findByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(1L, ticket.getUser().getId());
                    assertEquals(user, ticket.getUser());
                    assertEquals(bookTicketDto.screeningId(), ticket.getScreening().getId());
                    assertEquals(bookTicketDto.seatsIds().getFirst(), ticket.getSeat().getId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(film, hall);
        var seat1 = screening.getHall().getSeats().get(0);
        var seat2 = screening.getHall().getSeats().get(1);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat1.getId(), seat2.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.findAllByUserId(user.getId()))
                .isNotEmpty()
                .allSatisfy(ticket -> assertEquals(user, ticket.getUser()));
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithBookedTicket(film, hall, user);
        var seat = screening.getHall().getSeats().getFirst();
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new TicketAlreadyBookedException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(LocalDateTime.now(clock).minusMinutes(59), film, hall);
        var seat = screening.getHall().getSeats().getFirst();
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT + "/book")
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new TicketBookTooLateException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void ticket_is_cancelled() {
        //give
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(film, hall);
        var ticket = addTicket(screening, user);

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.findById(ticket.getId()))
                .isNotEmpty()
                .hasValueSatisfying(cancelledTicket -> assertNull(cancelledTicket.getUser()));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(LocalDateTime.now(clock).minusHours(23), film, hall);
        var ticket = addTicket(screening, user);

        //when
        var spec = webTestClient
                .patch()
                .uri(TICKETS_BASE_ENDPOINT + "/" + ticket.getId() + "/cancel")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        var expectedMessage = new TicketCancelTooLateException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void tickets_are_gotten_by_user_id() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreeningWithTickets(film, hall);
        var ticket = addTicket(screening, user);

        //when
        var spec = webTestClient
                .get()
                .uri(TICKETS_BASE_ENDPOINT + "/my")
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(everyItem(notNullValue()))
                .jsonPath("$[0].id").isEqualTo(ticket.getId())
                .jsonPath("$[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(hall.getId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().getRowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().getNumber())
                .jsonPath("$[0].userId").isEqualTo(user.getId());
    }
}
