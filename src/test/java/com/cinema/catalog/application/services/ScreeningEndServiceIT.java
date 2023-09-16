package com.cinema.catalog.application.services;

import com.cinema.SpringIT;
import com.cinema.catalog.ScreeningTestHelper;
import com.cinema.catalog.domain.ports.FilmRepository;
import com.cinema.catalog.domain.ports.RoomRepository;
import com.cinema.catalog.domain.ports.ScreeningReadOnlyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.RoomTestHelper.createRoom;
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
        ScreeningTestHelper.createScreenings(film, room).forEach(film::addScreening);
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
