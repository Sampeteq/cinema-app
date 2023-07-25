package code.catalog.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.services.ScreeningCreateService;
import code.catalog.domain.Screening;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.exceptions.ScreeningDateException;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.shared.time.TimeProvider;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static code.TimeHelper.getLocalDateTime;
import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.catalog.helpers.ScreeningTestHelper.getScreeningDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScreeningRestController_createScreening_IT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @MockInBean(ScreeningCreateService.class)
    private TimeProvider timeProvider;

    public static final String SCREENING_BASE_ENDPOINT = "/screenings";

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
                post(SCREENING_BASE_ENDPOINT)
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
                post(SCREENING_BASE_ENDPOINT)
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
                .perform(get(SCREENING_BASE_ENDPOINT))
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
                post(SCREENING_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateException(
                                "Screening date cannot be earlier than current"
                        ).getMessage()
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
                post(SCREENING_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateException(
                                "Difference between current and screening date " +
                                        "cannot be below " + 7 +
                                        " and above " + 21 + " days"
                        ).getMessage()
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
                post(SCREENING_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new ScreeningDateException(
                                "Difference between current and screening date " +
                                        "cannot be below " + 7 +
                                        " and above " + 21 + " days"
                        ).getMessage()
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
                post(SCREENING_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(screeningCreateDto))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new RoomsNoAvailableException().getMessage()));
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
}
