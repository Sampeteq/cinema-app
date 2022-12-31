package code.screenings;

import code.SpringIntegrationTests;
import code.films.FilmFacade;
import code.screenings.dto.BookSeatDto;
import code.screenings.dto.SeatBookingDto;
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

class SeatBookingIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private final Clock clock = Clock.systemUTC();

    @Test
    void should_booked_seat() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleBookTicketDTO = sampleBookSeatDTO(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var dto = fromResultActions(result, SeatBookingDto.class);
        mockMvc.perform(
                        get("/seats-bookings/" + dto.id())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(sampleBookTicketDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(sampleBookTicketDTO.lastName()))
                .andExpect(jsonPath("$.seat.seatId").value(sampleBookTicketDTO.seatId().toString()));
    }

    @Test
    void should_throw_exception_when_less_than_24h_to_screening() throws Exception {
        //given
        var sampleFilms = addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var sampleScreenings = screeningFacade.add(
                sampleAddScreeningDto(
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                ).withDate(LocalDateTime.now().minusHours(23))
        );
        var sampleBookTicketDTO = sampleBookSeatDTO(
                sampleScreenings.id(),
                sampleScreenings.seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Too late for seat booking: " + sampleBookTicketDTO.seatId()));
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleBookTicketDTO = sampleBookSeatDTO(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookTicketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        mockMvc.perform(
                get("/screenings")
        ).andExpect(jsonPath("$[0].freeSeats").value(sampleScreenings.get(0).freeSeats() - 1));
    }

    @Test
    void should_cancel_booking() throws Exception {
        //give
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = bookSampleSeat(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleTicket.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                screeningFacade.searchSeatBooking(sampleTicket.id())
                        .seat()
                        .status()
        ).isEqualTo(SeatStatus.FREE.name());
    }

    @Test
    void should_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = bookSampleSeat(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleTicket.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                get("/screenings")
        ).andExpect(jsonPath("$[0].freeSeats").value(sampleScreenings.get(0).freeSeats()));
    }

    @Test
    void should_throw_exception_when_booking_is_already_cancelled() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleTicket = bookSampleSeat(
                sampleScreenings.get(0).id(),
                sampleScreenings.get(0).seats().get(0).seatId()
        );
        screeningFacade.cancelSeatBooking(sampleTicket.id(), clock);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleTicket.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Seat not booked yet: " + sampleScreenings.get(0).seats().get(0).seatId()));
    }

    @Test
    void should_canceling_booking_be_impossible_when_less_than_24h_to_screening() throws Exception {
        //given
        var hoursUntilBooking = 23;
        var sampleFilmId = addSampleFilms(filmFacade).get(0).id();
        var sampleRoomId = addSampleScreeningRooms(screeningFacade).get(0);
        var sampleScreeningDate = LocalDateTime.now().minusHours(hoursUntilBooking);
        var sampleScreening = screeningFacade.add(
                sampleAddScreeningDto(
                        sampleFilmId,
                        sampleRoomId.id()
                ).withDate(sampleScreeningDate)
        );
        var timeDuringBooking = Clock.fixed(
                sampleScreeningDate.minusHours(hoursUntilBooking + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        var sampleTicket = screeningFacade.bookSeat(
                sampleBookSeatDTO(sampleScreening.id(), sampleScreening.seats().get(0).seatId()),
                timeDuringBooking
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleTicket.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking cancelling: " + sampleScreening.seats().get(0).seatId()
                ));
    }

    private static BookSeatDto sampleBookSeatDTO(UUID sampleScreeningId, UUID sampleSeatId) {
        return new BookSeatDto(
                sampleScreeningId,
                sampleSeatId,
                "Name 1",
                "Lastname 1"
        );
    }

    private SeatBookingDto bookSampleSeat(UUID sampleScreeningId, UUID sampleSeatId) {
        return screeningFacade.bookSeat(
                new BookSeatDto(
                        sampleScreeningId,
                        sampleSeatId,
                        "Name 1",
                        "Lastname 1"
                ),
                clock
        );
    }
}
