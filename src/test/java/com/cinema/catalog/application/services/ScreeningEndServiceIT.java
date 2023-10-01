package com.cinema.catalog.application.services;

import com.cinema.SpringIT;
import com.cinema.catalog.ScreeningTestHelper;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.ScreeningReadOnlyRepository;
import com.cinema.rooms.application.services.RoomFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static org.assertj.core.api.Assertions.assertThat;

class ScreeningEndServiceIT extends SpringIT {

    @Autowired
    private ScreeningEndService screeningFinishHandler;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomFacade roomFacade;

    @Autowired
    private ScreeningReadOnlyRepository screeningReadOnlyRepository;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        ScreeningTestHelper.createScreenings(film).forEach(film::addScreening);
        filmRepository.add(film);

        //when
        screeningFinishHandler.removeRoomsFromEndedScreenings();

        //then
        var screenings = screeningReadOnlyRepository.readAll();
        assertThat(screenings)
                .isNotEmpty()
                .allMatch(screening -> screening.getRoomId() == null);
    }
}
