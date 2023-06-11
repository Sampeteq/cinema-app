package code.films;

import code.SpringIT;
import code.films.application.dto.FilmScreeningSeatMapper;
import code.films.domain.FilmScreening;
import code.films.infrastructure.db.FilmRepository;
import code.rooms.infrastructure.db.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.FilmScreeningTestHelper.createScreening;
import static code.films.FilmTestHelper.createFilm;
import static code.rooms.RoomTestHelper.createRoom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmScreeningSeatRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmScreeningSeatMapper seatMapper;

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
