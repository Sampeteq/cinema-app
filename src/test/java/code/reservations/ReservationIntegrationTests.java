package code.reservations;

import code.SpringIntegrationTests;
import code.films.FilmFacade;
import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.TicketDTO;
import code.reservations.exception.ReservationAlreadyCancelled;
import code.reservations.exception.TooLateToCancelReservationException;
import code.reservations.exception.TooLateToReservationException;
import code.screenings.ScreeningFacade;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.exception.ScreeningNoFreeSeatsException;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static code.WebTestUtils.toJson;
import static code.films.FilmTestUtils.addSampleFilms;
import static code.screenings.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private final Clock clock = Clock.systemUTC();

    @Test
    void should_reserved_ticket() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleReserveTicketDTO = sampleReserveTicketDTO(sampleScreenings.get(0).id());

        //when
        var result = mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleReserveTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketUuid").exists())
                .andExpect(jsonPath("$.prize").exists())
                .andExpect(jsonPath("$.screeningId").value(sampleReserveTicketDTO.screeningId()))
                .andExpect(jsonPath("$.firstName").value(sampleReserveTicketDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(sampleReserveTicketDTO.lastName()))
                .andExpect(jsonPath("$.status").value(ScreeningTicketStatus.RESERVED.name()));
    }

    @Test
    void should_throw_exception_when_less_than_24h_to_screening() throws Exception {
        //given
        var sampleFilms = addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var sampleScreenings = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .date(LocalDateTime.now().minusHours(23))
                        .freeSeatsQuantity(200)
                        .minAge(13)
                        .filmId(sampleFilms.get(0).id())
                        .roomUuid(sampleRooms.get(0).uuid())
                        .build()
        );
        var sampleReserveTicketDTO = sampleReserveTicketDTO(sampleScreenings.id());

        //when
        var result = mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleReserveTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new TooLateToReservationException().getMessage()));
    }

    @Test
    void should_throw_exception_when_no_screening_free_seats() throws Exception {
        //given
        var sampleFilms = addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var sampleScreening = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .freeSeatsQuantity(0)
                        .date(LocalDateTime.now().plusHours(24))
                        .minAge(13)
                        .filmId(sampleFilms.get(0).id())
                        .roomUuid(sampleRooms.get(0).uuid())
                        .build()
        );
        var sampleReserveTicketDTO = sampleReserveTicketDTO(sampleScreening.id());

        //when
        var result = mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleReserveTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningNoFreeSeatsException(sampleScreening.id()).getMessage()));
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_reservation() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleReserveTicketDTO = sampleReserveTicketDTO(sampleScreenings.get(0).id());

        //when
        mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleReserveTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        mockMvc.perform(
                get("/screenings")
        ).andExpect(jsonPath("$[0].freeSeats").value(sampleScreenings.get(0).freeSeats() - 1));
    }

    @Test
    void should_cancel_ticket() throws Exception {
        //give
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketUuid() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                reservationFacade.readTicket(sampleTicket.ticketUuid()).status()
        ).isEqualTo(ScreeningTicketStatus.CANCELLED);
    }

    @Test
    void should_increase_screening_free_seats_by_one_after_ticket_reservation_cancelling() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = reserveSampleTicket(sampleScreenings.get(0).id());

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketUuid() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                get("/screenings")
        ).andExpect(jsonPath("$[0].freeSeats").value(sampleScreenings.get(0).freeSeats()));
    }

    @Test
    void should_throw_exception_when_ticket_is_already_cancelled() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket= reserveSampleTicket(sampleScreenings.get(0).id());
        reservationFacade.cancelTicket(sampleTicket.ticketUuid(), clock);

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketUuid() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ReservationAlreadyCancelled(sampleTicket.ticketUuid()).getMessage()));
    }

    @Test
    void should_canceling_reservation_be_impossible_when_less_than_24h_to_screening() throws Exception {
        //given
        var hoursUntilReservation = 23;
        var sampleFilmId = addSampleFilms(filmFacade).get(0).id();
        var sampleRoomUuid = addSampleScreeningRooms(screeningFacade).get(0).uuid();
        var sampleScreeningDate = LocalDateTime.now().minusHours(hoursUntilReservation);
        var sampleScreening = screeningFacade.add(
                sampleAddScreeningDTO(sampleFilmId, sampleRoomUuid).withDate(sampleScreeningDate)
        );
        var timeDuringReservation = Clock.fixed(
                sampleScreeningDate.minusHours(hoursUntilReservation + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        var sampleTicket = reservationFacade.reserveTicket(
                sampleReserveTicketDTO(sampleScreening.id()),
                timeDuringReservation
        );

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketUuid() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TooLateToCancelReservationException(sampleTicket.ticketUuid()).getMessage()
                ));
    }

    private static ReserveScreeningTicketDTO sampleReserveTicketDTO(Long sampleScreeningId) {
        return ReserveScreeningTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .build();
    }

    private TicketDTO reserveSampleTicket(Long sampleScreeningId) {
        return reservationFacade.reserveTicket(
                ReserveScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .build(),
                clock
        );
    }
}
