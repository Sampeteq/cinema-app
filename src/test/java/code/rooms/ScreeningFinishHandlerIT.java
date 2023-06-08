package code.rooms;

import code.screenings.ScreeningTestHelper;
import code.films.domain.FilmRepository;
import code.rooms.domain.RoomRepository;
import code.screenings.application.handlers.ScreeningFinishHandler;
import code.screenings.domain.Screening;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.FilmTestHelper.createFilm;
import static code.rooms.RoomTestHelper.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

class ScreeningFinishHandlerIT extends SpringIT {

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
        ScreeningTestHelper.createScreenings(film, room).forEach(room::addScreening);
        var screeningsWithRooms = roomRepository
                .add(createRoom())
                .getScreenings();

        //when
        screeningFinishHandler.handle();

        //then
        assertThat(screeningsWithRooms).noneMatch(Screening::hasRoom);
    }
}
