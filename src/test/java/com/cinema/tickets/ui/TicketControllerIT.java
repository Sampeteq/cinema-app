package com.cinema.tickets.ui;

import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
        var bookTicketDto = new BookTicketDto(
                nonExistingScreeningId,
                List.of(seatId)
        );


        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreening(hall, film);
        var nonExistingSeatId = 0L;
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(nonExistingSeatId)
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(hall, film);
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(screening.getTickets().getFirst().getSeat().getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.findByIdAndUserId(1L, 1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(1L, ticket.getUser().getId());
                    assertEquals(Ticket.Status.BOOKED, ticket.getStatus());
                    assertEquals(bookTicketDto.screeningId(), ticket.getScreening().getId());
                    assertEquals(bookTicketDto.seatsIds().getFirst(), ticket.getSeat().getId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(hall, film);
        var seat1 = screening.getHall().getSeats().get(0);
        var seat2 = screening.getHall().getSeats().get(1);
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat1.getId(), seat2.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(ticketRepository.findAllByUserId(user.getId()))
                .isNotEmpty()
                .allSatisfy(ticket -> assertEquals(Ticket.Status.BOOKED, ticket.getStatus()));
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithBookedFreeSeat(hall, film, user);
        var seat = screening.getHall().getSeats().getFirst();
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(LocalDateTime.now(clock).minusMinutes(59), hall, film);
        var seat = screening.getHall().getSeats().getFirst();
        var bookTicketDto = new BookTicketDto(
                screening.getId(),
                List.of(seat.getId())
        );

        //when
        var spec = webTestClient
                .post()
                .uri(TICKETS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(hall, film);
        var seat = screening.getHall().getSeats().getFirst();
        var ticket = addTicket(screening, seat, user);

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
                .hasValueSatisfying(cancelledTicket -> {
                            assertEquals(Ticket.Status.FREE, cancelledTicket.getStatus());
                            assertNull(cancelledTicket.getUser());
                        }
                );
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        //given
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(LocalDateTime.now(clock).minusHours(23), hall, film);
        var seat = screening.getHall().getSeats().getFirst();
        var ticket = addTicket(screening, seat, user);

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
        var hall = addHall();
        var film = addFilm();
        var screening = addScreeningWithTickets(hall, film);
        var seat = screening.getHall().getSeats().getFirst();
        var ticket = addTicket(screening, seat, user);

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
                .jsonPath("$.tickets[0].id").isEqualTo(ticket.getId())
                .jsonPath("$.tickets[0].status").isEqualTo(ticket.getStatus().name())
                .jsonPath("$.tickets[0].filmTitle").isEqualTo(film.getTitle())
                .jsonPath("$.tickets[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$.tickets[0].hallId").isEqualTo(hall.getId())
                .jsonPath("$.tickets[0].rowNumber").isEqualTo(seat.getRowNumber())
                .jsonPath("$.tickets[0].seatNumber").isEqualTo(seat.getNumber());
    }
}
