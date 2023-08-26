package code.catalog.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.ScreeningMapper;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.exceptions.ScreeningDateInPastException;
import code.catalog.domain.exceptions.ScreeningDateOutOfRangeException;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.shared.time.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

import static code.TimeHelper.getLocalDateTime;
import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.catalog.helpers.ScreeningTestHelper.createScreeningWithSpecificDate;
import static code.catalog.helpers.ScreeningTestHelper.createScreenings;
import static code.catalog.helpers.ScreeningTestHelper.getScreeningDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScreeningControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningMapper screeningMapper;

    @MockBean
    private TimeProvider timeProvider;

    public static final String SCREENINGS_BASE_ENDPOINT = "/screenings";

    @BeforeEach
    void setUpTimeProvider() {
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(new MockTimeProvider().getCurrentDate());
    }

    @Test
    @WithMockUser(authorities = "COMMON")
    void should_only_admin_create_screening() throws Exception {
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
    void should_create_screening() throws Exception {
        //given
        var film = filmRepository.add(createFilm());
        roomRepository.add(createRoom());
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
    void should_throw_exception_when_screening_date_is_earlier_than_current() throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomRepository.add(createRoom());
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
    void should_throw_exception_when_screening_and_current_date_difference_is_below_7_days()
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomRepository.add(createRoom());
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
    void should_throw_exception_when_screening_and_current_date_difference_is_above_21_days()
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        roomRepository.add(createRoom());
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
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var screening = prepareScreening();
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
    void should_read_all_screenings() throws Exception {
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
    void should_read_screenings_by_title() throws Exception {
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
    void should_read_screenings_by_category() throws Exception {
        //given
        var requiredFilmCategory = FilmCategory.COMEDY;
        var screeningWithRequiredFilmCategory = addScreening(() -> createFilm(requiredFilmCategory));
        addScreening(() -> createFilm(FilmCategory.DRAMA));

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "by/category")
                        .param("category", requiredFilmCategory.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredFilmCategory))));
    }

    @Test
    void should_read_screenings_by_date() throws Exception {
        //given
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(requiredDate.minusDays(1));

        //when
        var result = mockMvc.perform(
                get(SCREENINGS_BASE_ENDPOINT + "by/date")
                        .param("date", requiredDate.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredDate))));
    }

    private Screening prepareScreening() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var screening = createScreening(film, room, screeningDate);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }

    private ScreeningDto addScreening(Supplier<Film> filmSupplier) {
        var film = filmSupplier.get();
        var room = roomRepository.add(createRoom());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var screening = createScreening(film, room, screeningDate);
        film.addScreening(screening);
        var addedScreening = filmRepository
                .add(film)
                .getScreenings()
                .get(0);
        return screeningMapper.mapToDto(addedScreening);
    }

    private ScreeningDto addScreening(LocalDate date) {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreeningWithSpecificDate(film, room, dateTime);
        film.addScreening(screening);
        var addedScreening = filmRepository
                .add(film)
                .getScreenings()
                .get(0);
        return screeningMapper.mapToDto(addedScreening);
    }

    private List<ScreeningDto> addScreenings() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screenings = createScreenings(film, room);
        screenings.forEach(film::addScreening);
        return filmRepository
                .add(film)
                .getScreenings()
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
