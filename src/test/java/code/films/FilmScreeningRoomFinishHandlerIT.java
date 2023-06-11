package code.films;

import code.films.infrastructure.db.FilmRepository;
import code.films.infrastructure.db.FilmScreeningRoomRepository;
import code.films.application.handlers.FilmScreeningFinishHandler;
import code.films.domain.FilmScreening;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmScreeningRoomTestHelper.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

class FilmScreeningRoomFinishHandlerIT extends SpringIT {

    @Autowired
    private FilmScreeningFinishHandler screeningFinishHandler;

    @Autowired
    private FilmScreeningRoomRepository roomRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        var room = roomRepository.add(createRoom());
        FilmScreeningTestHelper.createScreenings(film, room).forEach(film::addScreening);
        var screeningsWithRooms = roomRepository
                .add(createRoom())
                .getScreenings();

        //when
        screeningFinishHandler.handle();

        //then
        assertThat(screeningsWithRooms).noneMatch(FilmScreening::hasRoom);
    }
}
