package com.cinema.screenings.application.rest;

import com.cinema.SpringIT;
import com.cinema.films.application.services.FilmService;
import com.cinema.screenings.application.dto.ScreeningCreateDto;
import com.cinema.screenings.application.dto.ScreeningDto;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.rooms.application.services.RoomService;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createFilmCreateDto;
import static com.cinema.screenings.ScreeningFixture.createRoomCreateDto;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static com.cinema.screenings.ScreeningFixture.getScreeningDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class ScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private FilmService filmService;

    @Autowired
    private RoomService roomService;

    @SpyBean
    private Clock clock;

    @Test
    void screening_is_created_only_by_admin() {
        //given
        addUser(UserRole.COMMON);

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void screening_is_created() {
        //given
        addUser(UserRole.ADMIN);
        var filmCreateDto = createFilmCreateDto();
        filmService.creteFilm(filmCreateDto);
        roomService.createRoom(createRoomCreateDto());
        var screeningCreateDto = new ScreeningCreateDto(SCREENING_DATE, filmCreateDto.title());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isCreated();
        var expectedDto = List.of(
               new ScreeningDto(
                       1L,
                       screeningCreateDto.date(),
                       createFilmCreateDto().title()
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
    void screening_and_current_date_difference_is_min_7_days() {
        //given
        addUser(UserRole.ADMIN);
        var filmCreateDto = createFilmCreateDto();
        filmService.creteFilm(filmCreateDto);
        roomService.createRoom(createRoomCreateDto());
        var screeningDate = LocalDateTime
                .now(clock)
                .plusDays(6);
        var screeningCreateDto = new ScreeningCreateDto(screeningDate, filmCreateDto.title());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
    void screening_and_current_date_difference_is_max_21_days() {
        //given
        addUser(UserRole.ADMIN);
        var filmCreateDto = createFilmCreateDto();
        filmService.creteFilm(filmCreateDto);
        roomService.createRoom(createRoomCreateDto());
        var screeningDate = LocalDateTime
                .now(clock)
                .plusDays(23);
        var screeningCreateDto = new ScreeningCreateDto(screeningDate, filmCreateDto.title());

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
    void screenings_collision_cannot_exist() {
        //given
        addUser(UserRole.ADMIN);
        filmService.creteFilm(createFilmCreateDto());
        var screening = addScreening();
        var screeningCreateDto = new ScreeningCreateDto(
                screening.getDate().plusMinutes(10),
                screening.getFilmTitle()
        );

        //when
        var spec = webTestClient
                .post()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(screeningCreateDto)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
    void screening_is_deleted_only_by_admin() {
        //given
        addUser(UserRole.COMMON);
        var screeningId = 1L;

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screeningId)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void screening_is_deleted() {
        //given
        addUser(UserRole.ADMIN);
        var screening = addScreening();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
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
                .jsonPath("$[0].filmTitle").isEqualTo(screening.getFilmTitle());
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
                        .path(SCREENINGS_BASE_ENDPOINT)
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
        addSeats();

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
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.number").exists()
                .jsonPath("$.*.status").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }

    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }

    private Screening addScreening(LocalDate date) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime);
        return screeningRepository.add(screening);
    }

    private void addSeats() {
        roomService.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(clock);
        screeningRepository.add(createScreening(screeningDate));
    }

    private void addUser(UserRole role) {
        userRepository.add(new User(USERNAME, passwordEncoder.encode(PASSWORD), role));
    }
}
