package code.films;

import code.SpringIT;
import code.films.application.dto.FilmMapper;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.films.infrastructure.db.FilmRepository;
import code.films.infrastructure.db.FilmScreeningRoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static code.films.FilmScreeningTestHelper.createScreening;
import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmTestHelper.createFilms;
import static code.films.FilmScreeningRoomTestHelper.createRoom;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmReadRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmScreeningRoomRepository roomRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Test
    void should_read_all_films() throws Exception {
        //given
        var addedFilms = addFilmsWithScreenings(() -> createFilms());

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
}
