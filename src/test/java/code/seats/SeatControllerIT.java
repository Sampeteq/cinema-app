package code.seats;

import code.films.domain.FilmRepository;
import code.seats.client.dto.SeatMapper;
import code.utils.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.utils.FilmTestHelper.createFilm;
import static code.utils.FilmTestHelper.createScreening;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SeatControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SeatMapper seatMapper;

    @Test
    void should_get_seats_for_screening() throws Exception {
        //given
        var film = createFilm();
        var screening = createScreening(film);
        film.addScreening(screening);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seatMapper.toDto(screening.getSeats()))));
    }
}
