package code.films;

import code.SpringIT;
import code.films.application.dto.SeatMapper;
import code.films.domain.Screening;
import code.films.infrastructure.db.FilmRepository;
import code.films.infrastructure.db.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.ScreeningTestHelper.createScreening;
import static code.films.FilmTestHelper.createFilm;
import static code.films.RoomTestHelper.createRoom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeatRestControllerIT extends SpringIT {

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
                get("/films/screenings/" + screening.getId() + "/seats")
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
