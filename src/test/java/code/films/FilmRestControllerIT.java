package code.films;

import code.SpringIT;
import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.application.dto.FilmScreeningDto;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.films.domain.FilmScreening;
import code.films.domain.exceptions.FilmScreeningRoomsNoAvailableException;
import code.films.domain.exceptions.FilmScreeningWrongDateException;
import code.films.domain.exceptions.FilmWrongYearException;
import code.films.infrastructure.db.FilmRepository;
import code.rooms.infrastructure.db.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static code.films.FilmScreeningTestHelper.SCREENING_DATE;
import static code.films.FilmScreeningTestHelper.createScreening;
import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmTestHelper.createFilmCreateCommand;
import static code.films.FilmTestHelper.createFilms;
import static code.rooms.RoomTestHelper.createRoom;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var cmd = createFilmCreateCommand();

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var createdFilm = fromResultActions(result, FilmDto.class);
        mockMvc
                .perform(get("/films"))
                .andExpect(content().json(toJson(List.of(createdFilm))));
    }

    @ParameterizedTest
    @MethodSource("code.films.FilmTestHelper#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var cmd = createFilmCreateCommand().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmWrongYearException().getMessage()));
    }

    @Test
    void should_read_all_films() throws Exception {
        //given
        var addedFilms = addFilmsWithScreenings();

        //when
        var result = mockMvc.perform(
                get("/films")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].screenings", everyItem(notNullValue())))
                .andExpect(content().json(toJson(filmMapper.mapToDto(addedFilms))));
    }

    @Test
    void should_read_film_by_title() throws Exception {
        //given
        var expectedTitle = "Film 1";
        var otherTitle = "Film 2";
        var expectedFilm = addFilmWithScreening(() -> createFilm(expectedTitle));
        addFilmWithScreening(() -> createFilm(otherTitle));

        //when
        var result = mockMvc.perform(
                get("/films/title")
                        .param("title", expectedTitle)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(filmMapper.mapToDto(expectedFilm))));
    }

    @Test
    void should_read_films_by_category() throws Exception {
        //given
        var expectedCategory = FilmCategory.COMEDY;
        var otherCategory = FilmCategory.DRAMA;
        var expectedFilms = addFilmsWithScreenings(() -> createFilms(expectedCategory));
        addFilmsWithScreenings(() -> createFilms(otherCategory));

        //when
        var result = mockMvc.perform(
                get("/films/category")
                        .param("category", expectedCategory.name())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(filmMapper.mapToDto(expectedFilms))));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = filmRepository.add(createFilm());
        var sampleRoom = roomRepository.add(createRoom());
        var screeningDate = SCREENING_DATE;

        //when
        var result = mockMvc.perform(
                post("/films/" + sampleFilm.getId() + "/screenings")
                        .param("screeningDate", screeningDate.toString())
        );

        //then
        result.andExpect(status().isCreated());
        var createdScreening = fromResultActions(result, FilmScreeningDto.class);
        mockMvc
                .perform(get("/films"))
                .andExpect(
                        jsonPath("$[0].screenings[0].id", equalTo(createdScreening.id().intValue()))
                )
                .andExpect(
                        jsonPath("$[0].screenings[0].date", equalTo(createdScreening.date().toString()))
                );
    }

    @ParameterizedTest
    @MethodSource("code.films.FilmScreeningTestHelper#getWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        var roomId = roomRepository.add(createRoom()).getId();

        //when
        var result = mockMvc.perform(
                post("/films/" + filmId + "/screenings")
                        .param("screeningDate", wrongDate.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmScreeningWrongDateException().getMessage()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var screening = prepareScreening();
        var screeningDate = screening.getDate().plusMinutes(10);

        //when
        var result = mockMvc.perform(
                post("/films/" + screening.getFilm().getId() + "/screenings")
                        .param("screeningDate", screeningDate.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmScreeningRoomsNoAvailableException().getMessage()));
    }

    private List<Film> addFilmsWithScreenings(Supplier<List<Film>> filmsSupplier) {
        var films = filmsSupplier.get();
        var room = roomRepository.add(createRoom());
        var screening1 = createScreening(
                films.get(0),
                room,
                LocalDateTime.of(2023,9,1,16,30)
        );
        var screening2 = createScreening(
                films.get(1),
                room,
                LocalDateTime.of(2023,9,3,18,30)
        );
        films.get(0).addScreening(screening1);
        films.get(1).addScreening(screening2);
        return films
                .stream()
                .map(filmRepository::add)
                .toList();
    }

    private List<Film> addFilmsWithScreenings() {
        return addFilmsWithScreenings(() -> createFilms());
    }

    private Film addFilmWithScreening(Supplier<Film> filmSupplier) {
        var film = filmSupplier.get();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(
                film,
                room,
                LocalDateTime.of(2023,9,1,16,30)
        );
        film.addScreening(screening);
        return filmRepository.add(film);
    }

    private FilmScreening prepareScreening() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }
}
