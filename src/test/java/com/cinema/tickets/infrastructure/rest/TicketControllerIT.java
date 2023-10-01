package com.cinema.tickets.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.shared.events.EventPublisher;
import com.cinema.tickets.TicketTestHelper;
import com.cinema.tickets.application.dto.TicketBookDto;
import com.cinema.tickets.application.dto.TicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketStatus;
import com.cinema.tickets.domain.events.TicketBookedEvent;
import com.cinema.tickets.domain.events.TicketCancelledEvent;
import com.cinema.tickets.domain.exceptions.TicketAlreadyCancelledException;
import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import com.cinema.tickets.domain.exceptions.TicketNotBelongsToUser;
import com.cinema.users.application.dto.UserSignUpDto;
import com.cinema.users.application.services.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.catalog.ScreeningTestHelper.getScreeningDate;
import static com.cinema.tickets.TicketTestHelper.createFilmCreateDto;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static com.cinema.tickets.TicketTestHelper.createScreeningCrateDto;
import static com.cinema.tickets.TicketTestHelper.prepareBookedTicket;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class TicketControllerIT extends SpringIT {

    private static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private RoomFacade roomFacade;

    @SpyBean
    private Clock clockMock;

    @MockBean
    private EventPublisher eventPublisher;

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
        result.andExpect(
                        status().isNotFound()
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
        result.andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void ticket_is_booked() throws Exception {
        //given
        var filmTitle = "Title 1";
        var roomId = "1";
        var screeningDate = getScreeningDate(clockMock);
        prepareSeat(filmTitle, roomId, screeningDate);
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
        result.andExpect(status().isCreated());
        assertThat(ticketRepository.readById(1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket -> {
                    assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
                    assertEquals(filmTitle, ticket.getFilmTitle());
                    assertEquals(screeningDate, ticket.getScreeningDate());
                    assertEquals(screeningId, ticket.getScreeningId());
                    assertEquals(roomId, ticket.getRoomId());
                    assertEquals(seatRowNumber, ticket.getSeatNumber());
                    assertEquals(seatNumber, ticket.getSeatNumber());
                    assertEquals(1L, ticket.getUserId());
                });
    }

    @Test
    void ticket_is_unique() throws Exception {
        //given
        prepareSeat();
        var ticket = ticketRepository.add(TicketTestHelper.prepareTicket());
        var ticketBookDto = new TicketBookDto(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );

        //when
        var result = mockMvc.perform(
                post(TICKETS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticketBookDto))
        );

        //then
        var expectedMessage = new TicketAlreadyExists().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void ticket_is_booked_at_least_1h_before_screening() throws Exception {
        //given
        var screeningDate = getScreeningDate(clockMock);
        prepareSeat(screeningDate);
        Mockito
                .when(clockMock.instant())
                .thenReturn(screeningDate.minusMinutes(59).toInstant(ZoneOffset.UTC));
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
        var expectedMessage = new TicketBookTooLateException().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void ticket_booked_event_is_published() throws Exception {
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
        var expectedEvent = new TicketBookedEvent(screeningId, rowNumber, seatNumber);
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_is_cancelled() throws Exception {
        //give
        prepareSeat();
        ticketRepository.add(prepareBookedTicket());

        //when
        var result = mockMvc.perform(
                patch(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(ticketRepository.readById(1L))
                .isNotEmpty()
                .hasValueSatisfying(ticket ->
                        assertEquals(TicketStatus.CANCELLED, ticket.getStatus())
                );
    }

    @Test
    void ticket_cancelled_event_is_published() throws Exception {
        //given
        prepareSeat();
        var ticket = ticketRepository.add(TicketTestHelper.prepareBookedTicket());

        //when
        mockMvc.perform(
                patch(TICKETS_BASE_ENDPOINT + "/" + 1 + "/cancel")
        );

        //then
        var expectedEvent = new TicketCancelledEvent(
                ticket.getScreeningId(),
                ticket.getRowNumber(),
                ticket.getSeatNumber()
        );
        verify(eventPublisher, times(1)).publish(expectedEvent);
    }

    @Test
    void ticket_already_cancelled_cannot_be_cancelled() throws Exception {
        //given
        prepareSeat();
        ticketRepository.add(TicketTestHelper.prepareCancelledTicket());

        //when
        var result = mockMvc.perform(
                patch(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        var expectedMessage = new TicketAlreadyCancelledException().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void ticket_is_cancelled_at_least_24h_before_screening() throws Exception {
        //given
        var screeningDate = getScreeningDate(clockMock);
        ticketRepository.add(TicketTestHelper.prepareBookedTicket(screeningDate));
        Mockito
                .when(clockMock.instant())
                .thenReturn(screeningDate.minusHours(23).toInstant(ZoneOffset.UTC));

        //when
        var result = mockMvc.perform(
                patch(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        var expectedMessage = new TicketCancelTooLateException().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void ticket_is_cancelled_if_belongs_to_current_user() throws Exception {
        //given
        var notCurrentUserId = 2L;
        ticketRepository.add(prepareBookedTicket(notCurrentUserId));

        //when
        var result = mockMvc.perform(
                patch(TICKETS_BASE_ENDPOINT + "/" + 1L + "/cancel")
        );

        //then
        var expectedMessage = new TicketNotBelongsToUser().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void tickets_are_read_by_user_id() throws Exception {
        //given
        var ticket = ticketRepository.add(TicketTestHelper.prepareBookedTicket());

        //when
        var result = mockMvc.perform(
                get("/tickets/my")
        );

        //then
        var expected = List.of(
                new TicketDto(
                        1L,
                        ticket.getStatus(),
                        ticket.getFilmTitle(),
                        ticket.getScreeningDate(),
                        ticket.getRoomId(),
                        ticket.getRowNumber(),
                        ticket.getSeatNumber()
                )
        );
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(expected)));
    }

    private void prepareSeat() {
        catalogFacade.createFilm(createFilmCreateDto());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(clockMock);
        catalogFacade.createScreening(createScreeningCrateDto(screeningDate));
    }

    private void prepareSeat(LocalDateTime screeningDate) {
        catalogFacade.createFilm(createFilmCreateDto());
        roomFacade.createRoom(createRoomCreateDto());
        catalogFacade.createScreening(createScreeningCrateDto(screeningDate));
    }

    private void prepareSeat(String filmTitle, String roomId, LocalDateTime screeningDate) {
        catalogFacade.createFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        roomFacade.createRoom(
                createRoomCreateDto().withId(roomId)
        );
        catalogFacade.createScreening(createScreeningCrateDto(screeningDate));
    }
}
