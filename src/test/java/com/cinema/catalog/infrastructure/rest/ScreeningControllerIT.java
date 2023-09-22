package com.cinema.catalog.infrastructure.rest;

import com.cinema.MockTimeProvider;
import com.cinema.SpringIT;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.ScreeningMapper;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.exceptions.ScreeningDateInPastException;
import com.cinema.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static com.cinema.TimeHelper.getLocalDateTime;
import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.ScreeningTestHelper.createScreening;
import static com.cinema.catalog.ScreeningTestHelper.createScreeningWithSpecificDate;
import static com.cinema.catalog.ScreeningTestHelper.createScreenings;
import static com.cinema.catalog.ScreeningTestHelper.getScreeningDate;
import static com.cinema.tickets.TicketTestHelper.createFilmCreateDto;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static com.cinema.tickets.TicketTestHelper.createScreeningCrateDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomFacade roomFacade;

    @Autowired
    private ScreeningMapper screeningMapper;

    @SpyBean
    private MockTimeProvider timeProvider;

    @Test
    @WithMockUser(authorities = "COMMON")
    void screening_is_created_only_by_admin() throws Exception {
        //given

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
        );

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_is_created() throws Exception {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningCreateDto = ScreeningCreateDto
                .builder()
                .filmId(film.getId())
                .date(getScreeningDate(timeProvider.getCurrentDate()))
                .build();

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result.andExpect(status().isCreated());
        var expectedDto = List.of(
               new ScreeningDto(
                       1L,
                       screeningCreateDto.date()
               )
        );
        mockMvc
                .perform(get(SCREENINGS_BASE_ENDPOINT))
                .andExpect(content().json(toJson(expectedDto)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_date_cannot_be_in_past() throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomFacade.createRoom(createRoomCreateDto());
        var currentDate = getLocalDateTime();
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(currentDate);
        var screeningCreateDto = ScreeningCreateDto
                .builder()
                .filmId(filmId)
                .date(currentDate.minusMinutes(1))
                .build();

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateInPastException().getMessage()
                ));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_and_current_date_difference_is_min_7_days() throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomFacade.createRoom(createRoomCreateDto());
        var currentDate = getLocalDateTime();
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(currentDate);
        var screeningCreateDto = ScreeningCreateDto
                .builder()
                .filmId(filmId)
                .date(currentDate.plusDays(6))
                .build();

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateOutOfRangeException().getMessage()
                ));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_and_current_date_difference_is_max_21_days() throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomFacade.createRoom(createRoomCreateDto());
        var currentDate = getLocalDateTime();
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(currentDate);
        var screeningCreateDto = ScreeningCreateDto
                .builder()
                .filmId(filmId)
                .date(currentDate.plusDays(22))
                .build();

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateOutOfRangeException().getMessage()
                ));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screenings_collision_cannot_exist() throws Exception {
        //given
        var screening = addScreening();
        var screeningCreateDto = ScreeningCreateDto
                .builder()
                .filmId(1L)
                .date(screening.getDate().plusMinutes(10))
                .build();

        //when
        var result = mockMvc.perform(
                post(SCREENINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new RoomsNoAvailableException().getMessage()));
    }

    @Test
    void screenings_are_read() throws Exception {
        //given
        var screenings = addScreenings();

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(screenings)));
    }

    @Test
    void screenings_are_read_by_film_title() throws Exception {
        //given
        var requiredFilmTitle = "Some title";
        var screeningWithRequiredFilmTitle = addScreening(() -> createFilm(requiredFilmTitle));
        addScreening(() -> createFilm("Some other title"));

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "/by/title")
                        .param("title", requiredFilmTitle)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredFilmTitle))));
    }

    @Test
    void screenings_are_read_by_film_category() throws Exception {
        //given
        var requiredFilmCategory = FilmCategory.COMEDY;
        var screeningWithRequiredFilmCategory = addScreening(() -> createFilm(requiredFilmCategory));
        addScreening(() -> createFilm(FilmCategory.DRAMA));

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "/by/category")
                        .param("category", requiredFilmCategory.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredFilmCategory))));
    }

    @Test
    void screenings_are_read_by_date() throws Exception {
        //given
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(requiredDate.minusDays(1));

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "/by/date")
                        .param("date", requiredDate.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredDate))));
    }

    @Test
    void seats_are_read_by_screening_id() throws Exception {
        //given
        var seats = prepareSeats();

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "/1/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    private Screening addScreening() {
        var film = createFilm();
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }

    private ScreeningDto addScreening(Supplier<Film> filmSupplier) {
        var film = filmSupplier.get();
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        var addedScreening = filmRepository
                .add(film)
                .getScreenings()
                .get(0);
        return screeningMapper.mapToDto(addedScreening);
    }

    private ScreeningDto addScreening(LocalDate date) {
        var film = createFilm();
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreeningWithSpecificDate(film, dateTime);
        film.addScreening(screening);
        var addedScreening = filmRepository
                .add(film)
                .getScreenings()
                .get(0);
        return screeningMapper.mapToDto(addedScreening);
    }

    private List<ScreeningDto> addScreenings() {
        var film = createFilm();
        var screenings = createScreenings(film);
        screenings.forEach(film::addScreening);
        return filmRepository
                .add(film)
                .getScreenings()
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }

    private List<SeatDto> prepareSeats() {
        catalogFacade.createFilm(createFilmCreateDto());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
        var screeningId = 1L;
        return catalogFacade.readSeatsByScreeningId(screeningId);
    }
}
