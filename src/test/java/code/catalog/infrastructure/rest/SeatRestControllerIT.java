package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.SeatMapper;
import code.catalog.domain.Screening;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeatRestControllerIT extends SpringIT {

    public static final String FILMS_SCREENINGS_ENDPOINT = "/films/screenings/";

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatMapper seatMapper;

    @Test
    void should_read_seats_for_screening() throws Exception {
        //given
        var screening = prepareScreening();

        //when
        var result = mockMvc.perform(
                get(FILMS_SCREENINGS_ENDPOINT + screening.getId() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seatMapper.toDto(screening.getSeats()))));
    }

    private Screening prepareScreening() {
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
