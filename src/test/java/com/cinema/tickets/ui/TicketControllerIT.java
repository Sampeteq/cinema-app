package com.cinema.tickets.ui;

import com.cinema.screenings.domain.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.domain.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketControllerIT extends TicketBaseIT {

    @Test
    void tickets_are_added() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var admin = addAdmin();

        //when
        var responseSpec = webTestClient
                .post()
                .uri("/admin" + TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .headers(headers -> headers.setBasicAuth(admin.getMail(), admin.getPassword()))
                .exchange();

        //then
        responseSpec.expectStatus().isOk();
        webTestClient
                .get()
                .uri("/public" + TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
                .exchange()
                .expectBody()
                .jsonPath("$").value(hasSize(hall.getSeats().size()))
                .jsonPath("$.*.filmTitle").value(everyItem(equalTo(film.getTitle())))
                .jsonPath("$.*.screeningDate").value(everyItem(equalTo(screening.getDate().toString())))
                .jsonPath("$.*.hallId").value(everyItem(equalTo(hall.getId().intValue())))
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.seatNumber").exists();
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
        var user = addUser();

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
        var user = addUser();

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
        var screening = addScreening(film, hall);
        var ticket = addTicket(screening);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat().getId())
        );
        var user = addUser();

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
                .hasValueSatisfying(bookedTicket -> {
                    assertEquals(1L, bookedTicket.getUser().getId());
                    assertEquals(user, bookedTicket.getUser());
                    assertEquals(bookTicketDto.screeningId(), bookedTicket.getScreening().getId());
                    assertEquals(bookTicketDto.seatsIds().getFirst(), bookedTicket.getSeat().getId());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var tickets = addTickets(screening);
        var seat1 = tickets.get(0).getSeat();
        var seat2 = tickets.get(1).getSeat();
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat1.getId(), seat2.getId())
        );
        var user = addUser();

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
        var screening = addScreening(film, hall);
        var user = addUser();
        var ticket = addTicket(screening, user);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat().getId())
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
        var screening = addScreening(LocalDateTime.now(clock).minusMinutes(59), film, hall);
        var user = addUser();
        var ticket = addTicket(screening, user);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat().getId())
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
        var screening = addScreening(film, hall);
        var user = addUser();
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
        var screening = addScreening(LocalDateTime.now(clock).minusHours(23), film, hall);
        var user = addUser();
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
        var screening = addScreening(film, hall);
        var user = addUser();
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

    @Test
    void tickets_are_gotten_by_screening_id() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var ticket = addTicket(screening);

        //when
        var spec = webTestClient
                .get()
                .uri("/public"+ TICKETS_BASE_ENDPOINT + "?screeningId=" + screening.getId())
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
                .jsonPath("$[0].userId").value(Matchers.nullValue());
    }
}
