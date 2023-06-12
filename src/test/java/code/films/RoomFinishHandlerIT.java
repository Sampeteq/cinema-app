package code.films;

import code.films.infrastructure.db.FilmRepository;
import code.films.infrastructure.db.RoomRepository;
import code.films.application.handlers.ScreeningFinishHandler;
import code.films.domain.Screening;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.FilmTestHelper.createFilm;
import static code.films.RoomTestHelper.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

class RoomFinishHandlerIT extends SpringIT {

    @Autowired
    private ScreeningFinishHandler screeningFinishHandler;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        var room = roomRepository.add(createRoom());
        ScreeningTestHelper.createScreenings(film, room).forEach(film::addScreening);
        var screeningsWithRooms = roomRepository
                .add(createRoom())
                .getScreenings();

        //when
        screeningFinishHandler.handle();

        //then
        assertThat(screeningsWithRooms).noneMatch(Screening::hasRoom);
    }
}
