package com.cinema.tickets;

import com.cinema.halls.Seat;
import com.cinema.screenings.exceptions.ScreeningNotFoundException;
import com.cinema.screenings.exceptions.ScreeningSeatNotFoundException;
import com.cinema.tickets.exceptions.TicketAlreadyBookedException;
import com.cinema.tickets.exceptions.TicketBookTooLateException;
import com.cinema.tickets.exceptions.TicketCancelTooLateException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TicketControllerIT extends TicketBaseIT {

    @Test
    void tickets_are_added() {
        //given
        var hall = addHall();
        var screening = addScreening(hall.getId());
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
                .jsonPath("$.*.filmTitle").value(everyItem(equalTo(screening.getFilmTitle())))
                .jsonPath("$.*.screeningDate").value(everyItem(equalTo(screening.getDate().toString())))
                .jsonPath("$.*.hallId").value(everyItem(equalTo(hall.getId().intValue())))
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.seatNumber").exists();
    }

    @Test
    void ticket_is_booked_for_existing_screening() {
        //given
        var nonExistingScreeningId = 0L;
        var bookTicketDto = new TicketBookRequest(
                nonExistingScreeningId,
                List.of(new Seat(1, 1))
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
        var screening = addScreening();
        var nonExistingSeatId = new Seat(0,0);
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
        var screening = addScreening();
        var ticket = addTicket(screening);
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
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
                    assertEquals(1L, bookedTicket.getUserId());
                    assertEquals(user.getId(), bookedTicket.getUserId());
                    assertEquals(bookTicketDto.screeningId(), bookedTicket.getScreening().getId());
                    assertEquals(bookTicketDto.seats().getFirst(), bookedTicket.getSeat());
                });
    }

    @Test
    void tickets_are_booked() {
        //given
        var screening = addScreening();
        var tickets = addTickets(screening);
        var seat1 = tickets.get(0).getSeat();
        var seat2 = tickets.get(1).getSeat();
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(seat1, seat2)
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
                .allSatisfy(ticket -> assertEquals(user.getId(), ticket.getUserId()));
    }

    @Test
    void ticket_is_booked_for_free_seat() {
        //given
        var screening = addScreening();
        var user = addUser();
        var ticket = addTicket(screening, user.getId());
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
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
        var screening = addScreening(LocalDateTime.now(clock).minusMinutes(59));
        var user = addUser();
        var ticket = addTicket(screening, user.getId());
        var bookTicketDto = new TicketBookRequest(
                screening.getId(),
                List.of(ticket.getSeat())
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
        var screening = addScreening();
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

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
                .hasValueSatisfying(cancelledTicket -> assertNull(cancelledTicket.getUserId()));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() {
        //given
        var screening = addScreening(LocalDateTime.now(clock).minusHours(23));
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

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
        var screening = addScreening();
        var user = addUser();
        var ticket = addTicket(screening, user.getId());

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
                .jsonPath("$[0].filmTitle").isEqualTo(ticket.getScreening().getFilmTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").isEqualTo(user.getId());
    }

    @Test
    void tickets_are_gotten_by_screening_id() {
        //given
        var screening = addScreening();
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
                .jsonPath("$[0].filmTitle").isEqualTo(ticket.getScreening().getFilmTitle())
                .jsonPath("$[0].screeningDate").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].hallId").isEqualTo(screening.getHallId())
                .jsonPath("$[0].rowNumber").isEqualTo(ticket.getSeat().rowNumber())
                .jsonPath("$[0].seatNumber").isEqualTo(ticket.getSeat().number())
                .jsonPath("$[0].userId").value(Matchers.nullValue());
    }
}
