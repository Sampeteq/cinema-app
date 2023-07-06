package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.ScreeningMapper;
import code.catalog.domain.Film;
import code.catalog.domain.FilmCategory;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.function.Supplier;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.FilmTestHelper.createFilms;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.catalog.helpers.ScreeningTestHelper.createScreenings;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmReadPublicRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private ScreeningMapper screeningMapper;

    public static final String FILMS_BASE_ENDPOINT = "/films/";

    private static final int CURRENT_YEAR = Year.now().getValue();

    @Test
    void should_read_all_films() throws Exception {
        //given
        var films = addFilms();

        //when
        var result = mockMvc.perform(
                get(FILMS_BASE_ENDPOINT)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(films)));
    }

    @Test
    void should_read_all_screenings() throws Exception {
        //given
        var screenings = addScreenings();

        //when
        var result = mockMvc.perform(
                get(FILMS_BASE_ENDPOINT + "/screenings")
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
                get(FILMS_BASE_ENDPOINT + "/screenings/by" + "/title")
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
                get(FILMS_BASE_ENDPOINT + "/screenings/by" + "/category")
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
        var requiredDate = LocalDate.of(CURRENT_YEAR + 1, 1, 1);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(LocalDate.of(CURRENT_YEAR + 1, 1, 2));

        //when
        var result = mockMvc.perform(
                get(FILMS_BASE_ENDPOINT + "/screenings/by" + "/date")
                        .param("date", requiredDate.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningWithRequiredDate))));
    }

    private List<FilmDto> addFilms() {
        return createFilms()
                .stream()
                .map(filmRepository::add)
                .map(film -> filmMapper.mapToDto(film))
                .toList();
    }

    private ScreeningDto addScreening(Supplier<Film> filmSupplier) {
        var film = filmSupplier.get();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
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
        var screening = createScreening(film, room, dateTime);
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
