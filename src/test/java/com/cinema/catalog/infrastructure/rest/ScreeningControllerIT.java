package com.cinema.catalog.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.catalog.ScreeningTestHelper;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.application.dto.ScreeningDto;
import com.cinema.catalog.application.dto.SeatDto;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.Screening;
import com.cinema.catalog.domain.ScreeningRepository;
import com.cinema.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomFacade;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.ScreeningTestHelper.createScreening;
import static com.cinema.catalog.ScreeningTestHelper.getScreeningDate;
import static com.cinema.tickets.TicketTestHelper.createFilmCreateDto;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class ScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private RoomFacade roomFacade;

    @SpyBean
    private Clock clockMock;

    @Test
    @WithMockUser(authorities = "COMMON")
    void screening_is_created_only_by_admin() {
        //given

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_is_created() {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningCreateDto = new ScreeningCreateDto(getScreeningDate(clockMock), film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .exchange();

        //then
        spec.expectStatus().isCreated();
        var expectedDto = List.of(
               new ScreeningDto(
                       1L,
                       screeningCreateDto.date(),
                       film.getTitle(),
                       film.getCategory()
               )
        );
        webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange()
                .expectBody()
                .json(toJson(expectedDto));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = LocalDateTime
                .now(clockMock)
                .plusDays(6);
        var screeningCreateDto = new ScreeningCreateDto(screeningDate, film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .exchange();

        //then
        var expectedMessage = new ScreeningDateOutOfRangeException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_and_current_date_difference_is_max_21_days() {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = LocalDateTime
                .now(clockMock)
                .plusDays(22);
        var screeningCreateDto = new ScreeningCreateDto(screeningDate, film.getTitle());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .exchange();

        //then
        var expectedMessage = new ScreeningDateOutOfRangeException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screenings_collision_cannot_exist() {
        //given
        var screening = addScreening();
        var screeningCreateDto = new ScreeningCreateDto(
                screening.getDate().plusMinutes(10),
                screening.getFilm().getTitle()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .exchange();

        //then
        var expectedMessage = new RoomsNoAvailableException().getMessage();
        spec
                .expectStatus()
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void screening_is_deleted_only_by_admin() {
        //given
        var screeningId = 1L;

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screeningId)
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void screening_is_deleted() {
        //given
        var screening = addScreening();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screening.getId())
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(screeningRepository.readById(screening.getId())).isEmpty();
    }

    @Test
    void screenings_are_read() {
        //given
        var screening = addScreening();

        //when
        var spec = webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(screening.getFilm().getTitle());
    }

    @Test
    void screenings_are_read_by_film_title() {
        //given
        var requiredFilmTitle = "Some film title";
        var otherFilmTitle = "Some other film title";
        addScreening(requiredFilmTitle);
        addScreening(otherFilmTitle);

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_BASE_ENDPOINT + "/by/title")
                        .queryParam("title", requiredFilmTitle)
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[*].filmTitle").isEqualTo(requiredFilmTitle);
    }

    @Test
    void screenings_are_read_by_film_category() {
        //given
        var requiredFilmCategory = FilmCategory.COMEDY;
        var otherFilmCategory = FilmCategory.DRAMA;
        addScreening(requiredFilmCategory);
        addScreening(otherFilmCategory);

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_BASE_ENDPOINT + "/by/category")
                        .queryParam("category", requiredFilmCategory.name())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[*].filmCategory").isEqualTo(requiredFilmCategory.name());
    }

    @Test
    void screenings_are_read_by_date() {
        //given
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(requiredDate.minusDays(1));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_BASE_ENDPOINT + "/by/date")
                        .queryParam("date", requiredDate.toString())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }

    @Test
    void seats_are_read_by_screening_id() {
        //given
        var seats = prepareSeats();

        //when
        var spec = webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT + "/1/seats")
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .json(toJson(seats));
    }

    private Screening addScreening() {
        var film = createFilm();
        var screeningDate = getScreeningDate(clockMock);
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }

    private void addScreening(String filmTitle) {
        var film = createFilm(filmTitle);
        var screeningDate = getScreeningDate(clockMock);
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        filmRepository
                .add(film);
    }

    private void addScreening(FilmCategory filmCategory) {
        var film = createFilm(filmCategory);
        var screeningDate = getScreeningDate(clockMock);
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        filmRepository
                .add(film);
    }

    private Screening addScreening(LocalDate date) {
        var film = createFilm();
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = ScreeningTestHelper.createScreening(film, dateTime);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }

    private List<SeatDto> prepareSeats() {
        var filmCreateDto = createFilmCreateDto();
        catalogFacade.createFilm(filmCreateDto);
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(clockMock);
        catalogFacade.createScreening(
                new ScreeningCreateDto(
                        screeningDate,
                        filmCreateDto.title()
                )
        );
        var screeningId = 1L;
        return catalogFacade.readSeatsByScreeningId(screeningId);
    }
}
