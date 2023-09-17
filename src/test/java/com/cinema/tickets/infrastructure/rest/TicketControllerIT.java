package com.cinema.tickets.infrastructure.rest;

import com.cinema.MockTimeProvider;
import com.cinema.SpringIT;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.services.TicketFacade;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.user.application.dto.UserSignUpDto;
import com.cinema.user.application.services.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.cinema.tickets.TicketTestHelper.createFilmCreateDto;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static com.cinema.tickets.TicketTestHelper.createScreeningCrateDto;
import static com.cinema.catalog.ScreeningTestHelper.getScreeningDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class TicketControllerIT extends SpringIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private TicketFacade ticketFacade;

    @SpyBean
    private MockTimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        var username = "user1@mail.com";
        var password = "12345";
        userFacade.signUpUser(
                new UserSignUpDto(
                        username,
                        password,
                        password
                )
        );
    }

    @Test
    void seats_are_read_by_screening_id() throws Exception {
        //given
        var seats = prepareSeats();

        //when
        var result = mockMvc.perform(
                get(TICKETS_BASE_ENDPOINT + "/seats").param("screeningId", "1")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    @Test
    void ticket_is_made_for_existing_screening() throws Exception {
        //given
        var nonExistingScreeningId = 0L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                nonExistingScreeningId,
                seatRowNumber,
                seatNumber
        );


        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .content(toJson(ticketBookDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        content().string(
                                new EntityNotFoundException("Screening").getMessage()
                        )
                );
    }

    @Test
    void ticket_is_booked_for_existing_seat() throws Exception {
        //given
        prepareSeat();
        var screeningId = 1L;
        var nonExistingSeatRowNumber = 100;
        var nonExistingSeatNumber = 100;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                nonExistingSeatRowNumber,
                nonExistingSeatNumber
        );


        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .content(toJson(ticketBookDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        content().string(
                                new EntityNotFoundException("Seat").getMessage()
                        )
                );
    }

    @Test
    void ticket_is_booked() throws Exception {
        //given
        var filmTitle = "Title 1";
        var roomCustomId = "1";
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareSeat(filmTitle, roomCustomId, screeningDate);
        var screeningId = 1L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                seatRowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticketBookDto))
        );

        //then
        result.andExpect(status().isOk());
        var expectedDto = List.of(
                new TicketDto(
                        1L,
                        TicketStatus.ACTIVE,
                        filmTitle,
                        screeningDate,
                        roomCustomId,
                        seatRowNumber,
                        seatNumber
                )
        );
        mockMvc.perform(
                get(TICKETS_BASE_ENDPOINT + "/my")
        ).andExpect(content().json(toJson(expectedDto)));
    }

    @Test
    void ticket_is_unique() throws Exception {
        //given
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        prepareTicket(screeningId, rowNumber, seatNumber);
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticketBookDto))
        );

        //then
        result
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        content().string(
                                new TicketAlreadyExists().getMessage()
                        )
                );
    }

    @Test
    void tikcet_is_booked_at_least_1h_before_screening() throws Exception {
        //given
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareSeat(screeningDate);
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.minusMinutes(59));
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticketBookDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TicketBookTooLateException().getMessage()
                ));
    }

    @Test
    void ticket_booking_makes_seat_not_free() throws Exception {
        //given
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticketBookDto))
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get(TICKETS_BASE_ENDPOINT + "/seats").param("screeningId", "1")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.id().equals(1L) && !s.isFree()
        );
        assertThat(isSeatBusy).isTrue();
    }

    @Test
    void ticket_is_cancelled() throws Exception {
        //give
        prepareTicket();

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchTicketsResult = mockMvc.perform(
                get(TICKETS_BASE_ENDPOINT + "/my")
        );
        var isTicketCancelled = getTicketsFromResult(searchTicketsResult).anyMatch(
                b -> b.id().equals(1L) && b.status().equals(TicketStatus.CANCELLED)
        );
        assertThat(isTicketCancelled).isTrue();
    }

    @Test
    void ticket_cancel_makes_seat_free_again() throws Exception {
        //given
        prepareTicket();

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT + "/" + 1 + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get(TICKETS_BASE_ENDPOINT + "/seats").param("screeningId", "1")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(s -> s.id().equals(1L) && s.isFree());
        assertThat(isSeatFreeAgain).isTrue();
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() throws Exception {
        //given
        prepareCancelledTicket();

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TicketAlreadyCancelledException().getMessage()
                ));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() throws Exception {
        //given
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareTicket(screeningDate);
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.minusHours(23));

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TicketCancelTooLateException().getMessage()
                ));
    }

    @Test
    void tickets_are_read_by_user_id() throws Exception {
        //given
        var filmTitle = "Title 1";
        var roomCustomId = "1";
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var seatRowNumber = 1;
        var seatNumber = 1;
        prepareTicket(filmTitle, roomCustomId, screeningDate);

        //when
        var result = mockMvc.perform(
                get("/tickets/my")
        );

        //then
        result.andExpect(status().isOk());
        var ticketsFromResult = getTicketsFromResult(result).toList();
        var expected = List.of(
                new TicketDto(
                        1L,
                        TicketStatus.ACTIVE,
                        filmTitle,
                        screeningDate,
                        roomCustomId,
                        seatRowNumber,
                        seatNumber
                )
        );
        assertThat(ticketsFromResult).containsExactlyInAnyOrderElementsOf(expected);
    }

    private List<SeatDto> prepareSeats() {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
        var screeningId = 1L;
        return ticketFacade.readSeatsByScreeningId(screeningId);
    }

    private void prepareSeat() {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(LocalDateTime screeningDate) {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        catalogFacade.createFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        catalogFacade.createRoom(
                createRoomCreateDto().withCustomId(roomCustomId)
        );
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareTicket() {
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var dto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        ticketFacade.bookTicket(dto);
    }

    private void prepareTicket(Long screeningId, int rowNumber, int seatNumber) {
        prepareSeat();
        var dto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        ticketFacade.bookTicket(dto);
    }

    private void prepareTicket(LocalDateTime screeningDate) {
        prepareSeat(screeningDate);
        var screeningId = 1L;
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.plusHours(25));
        var rowNumber = 1;
        var seatNumber =  1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        ticketFacade.bookTicket(ticketBookDto);
    }

    private void prepareTicket(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        prepareSeat(filmTitle, roomCustomId, screeningDate);
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber =  1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        ticketFacade.bookTicket(ticketBookDto);
    }

    private void prepareCancelledTicket() {
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber =  1;
        var ticketBookDto = new TicketBookDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        ticketFacade.bookTicket(ticketBookDto);
        var ticketId = 1L;
        ticketFacade.cancelTicket(ticketId);
    }

    private Stream<SeatDto> getSeatsFromResult(ResultActions searchSeatsResult) {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, SeatDto[].class)
        );
    }

    private Stream<TicketDto> getTicketsFromResult(ResultActions searchSeatsResult) {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, TicketDto[].class)
        );
    }
}
