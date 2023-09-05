package code.catalog.application.services;

import code.SpringIT;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.catalog.domain.ports.ScreeningReadOnlyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.catalog.FilmTestHelper.createFilm;
import static code.catalog.RoomTestHelper.createRoom;
import static code.catalog.ScreeningTestHelper.createScreenings;
import static org.assertj.core.api.Assertions.assertThat;

class ScreeningEndServiceIT extends SpringIT {

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
        filmRepository.add(film);

        //when
        screeningFinishHandler.removeRoomsFromEndedScreenings();

        //then
        var screenings = screeningReadOnlyRepository.readAll();
        assertThat(screenings)
                .isNotEmpty()
                .allMatch(screening -> screening.getRoom() == null);
    }
}
