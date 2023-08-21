package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.FilmMapper;
import code.catalog.domain.ports.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static code.catalog.helpers.FilmTestHelper.createFilms;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmController_readFilms_IT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    public static final String FILMS_BASE_ENDPOINT = "/films/";

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

    private List<FilmDto> addFilms() {
        return createFilms()
                .stream()
                .map(filmRepository::add)
                .map(film -> filmMapper.mapToDto(film))
                .toList();
    }
}
