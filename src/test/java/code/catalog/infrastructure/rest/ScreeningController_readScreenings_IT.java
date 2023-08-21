package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.ScreeningMapper;
import code.catalog.application.dto.SeatMapper;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.Screening;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.shared.time.TimeProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.function.Supplier;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.catalog.helpers.ScreeningTestHelper.createScreeningWithSpecificDate;
import static code.catalog.helpers.ScreeningTestHelper.createScreenings;
import static code.catalog.helpers.ScreeningTestHelper.getScreeningDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScreeningController_readScreenings_IT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningMapper screeningMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private TimeProvider timeProvider;

    public static final String SCREENINGS_BASE_ENDPOINT = "/screenings/";

    private static final int CURRENT_YEAR = Year.now().getValue();

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

    private Screening addScreening() {
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
