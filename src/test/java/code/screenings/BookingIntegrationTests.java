package code.screenings;

import code.SpringIntegrationTests;
import code.films.FilmFacade;
import code.screenings.dto.BookScreeningTicketDTO;
import code.screenings.dto.ScreeningTicketStatusDTO;
import code.screenings.dto.ScreeningTicketDTO;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static code.WebTestUtils.fromResultActions;
import static code.WebTestUtils.toJson;
import static code.films.FilmTestUtils.addSampleFilms;
import static code.screenings.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private final Clock clock = Clock.systemUTC();

    @Test
    void should_booked_ticket() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleBookTicketDTO = sampleBookTicketDTO(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleBookTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var dto = fromResultActions(result, ScreeningTicketDTO.class);
        mockMvc.perform(
          get("/screenings-tickets/" + dto.ticketId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").exists())
                .andExpect(jsonPath("$.screeningId").value(sampleBookTicketDTO.screeningId().toString()))
                .andExpect(jsonPath("$.seatId").value(sampleBookTicketDTO.seatId().toString()))
                .andExpect(jsonPath("$.firstName").value(sampleBookTicketDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(sampleBookTicketDTO.lastName()))
                .andExpect(jsonPath("$.status").value(ScreeningTicketStatusDTO.BOOKED.name()));
    }

    @Test
    void should_throw_exception_when_less_than_24h_to_screening() throws Exception {
        //given
        var sampleFilms = addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var sampleScreenings = screeningFacade.add(
                sampleAddScreeningDTO(
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                ).withDate(LocalDateTime.now().minusHours(23))
        );
        var sampleBookTicketDTO = sampleBookTicketDTO(
                sampleScreenings.id(),
                sampleScreenings.seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleBookTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new TooLateToBookingException().getMessage()));
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_ticket_booking() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleBookTicketDTO = sampleBookTicketDTO(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        mockMvc.perform(
                post("/screenings-tickets")
                        .content(toJson(sampleBookTicketDTO))
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
        var sampleTicket = bookSampleTicket(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                screeningFacade.readTicket(sampleTicket.ticketId()).status()
        ).isEqualTo(ScreeningTicketStatusDTO.CANCELLED);
    }

    @Test
    void should_increase_screening_free_seats_by_one_after_ticket_booking_cancelling() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = bookSampleTicket(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketId() + "/cancel")
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
        var sampleTicket = bookSampleTicket(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );
        screeningFacade.cancelTicket(sampleTicket.ticketId(), clock);

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketId() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new BookingAlreadyCancelledException(sampleTicket.ticketId()).getMessage()));
    }

    @Test
    void should_canceling_booking_be_impossible_when_less_than_24h_to_screening() throws Exception {
        //given
        var hoursUntilBooking = 23;
        var sampleFilmId = addSampleFilms(filmFacade).get(0).id();
        var sampleRoomId = addSampleScreeningRooms(screeningFacade).get(0);
        var sampleScreeningDate = LocalDateTime.now().minusHours(hoursUntilBooking);
        var sampleScreening = screeningFacade.add(
                sampleAddScreeningDTO(
                        sampleFilmId,
                        sampleRoomId.id()
                ).withDate(sampleScreeningDate)
        );
        var timeDuringBooking = Clock.fixed(
                sampleScreeningDate.minusHours(hoursUntilBooking + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        var sampleTicket = screeningFacade.bookTicket(
                sampleBookTicketDTO(sampleScreening.id(), sampleScreening.seats().get(0).seatId()),
                timeDuringBooking
        );

        //when
        var result = mockMvc.perform(
                patch("/screenings-tickets/" + sampleTicket.ticketId() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TooLateToCancelBookingException(sampleTicket.ticketId()).getMessage()
                ));
    }

    private static BookScreeningTicketDTO sampleBookTicketDTO(UUID sampleScreeningId, UUID sampleScreeningSeatId) {
        return BookScreeningTicketDTO
                .builder()
                .screeningId(sampleScreeningId)
                .seatId(sampleScreeningSeatId)
                .firstName("Name 1")
                .lastName("Lastname 1")
                .build();
    }

    private ScreeningTicketDTO bookSampleTicket(UUID sampleScreeningId, UUID sampleSeatId) {
        return screeningFacade.bookTicket(
                BookScreeningTicketDTO
                        .builder()
                        .screeningId(sampleScreeningId)
                        .seatId(sampleSeatId)
                        .firstName("Name")
                        .lastName("Lastname")
                        .build(),
                clock
        );
    }
}
