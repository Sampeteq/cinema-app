package code.catalog.application;

import code.SpringIT;
import code.catalog.application.services.ScreeningEndService;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.catalog.domain.ports.ScreeningReadOnlyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreenings;
import static org.assertj.core.api.Assertions.assertThat;

class RoomFinishHandlerIT extends SpringIT {

    @Autowired
    private ScreeningEndService screeningFinishHandler;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningReadOnlyRepository screeningReadOnlyRepository;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        var room = roomRepository.add(createRoom());
        createScreenings(film, room).forEach(film::addScreening);

        //when
        screeningFinishHandler.removeRoomsFromEndedScreenings();

        //then
        var screenings = screeningReadOnlyRepository.readAll();
        assertThat(screenings).allMatch(screening -> screening.getRoom() == null);
    }
}
