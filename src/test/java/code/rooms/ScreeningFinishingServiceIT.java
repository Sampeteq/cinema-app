package code.rooms;

import code.films.domain.FilmRepository;
import code.rooms.domain.RoomRepository;
import code.screenings.application.services.ScreeningFinishingService;
import code.screenings.domain.Screening;
import code.utils.SpringIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static code.utils.FilmTestHelper.createFilm;
import static code.utils.FilmTestHelper.createScreenings;
import static code.utils.RoomTestHelper.createRoom;
import static org.assertj.core.api.Assertions.assertThat;

class ScreeningFinishingServiceIT extends SpringIT {

    @Autowired
    private ScreeningFinishingService screeningFinishingService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        var room = roomRepository.add(createRoom());
        createScreenings(film, room).forEach(room::addScreening);
        var screeningsWithRooms = roomRepository
                .add(createRoom())
                .getScreenings();

        //when
        screeningFinishingService.onFinishedScreenings();

        //then
        assertThat(screeningsWithRooms).noneMatch(Screening::hasRoom);
    }
}
