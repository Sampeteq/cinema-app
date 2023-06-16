package code.catalog.application;

import code.catalog.helpers.ScreeningTestHelper;
import code.catalog.infrastructure.db.FilmRepository;
import code.catalog.infrastructure.db.RoomRepository;
import code.catalog.application.services.ScreeningEndService;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

class RoomFinishHandlerIT extends SpringIT {

    @Autowired
    private ScreeningEndService screeningFinishHandler;

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
        screeningFinishHandler.removeRoomsFromEndedScreenings();

        //then
        assertThat(screeningsWithRooms).allMatch(screening -> screening.getRoom() == null);
    }
}
