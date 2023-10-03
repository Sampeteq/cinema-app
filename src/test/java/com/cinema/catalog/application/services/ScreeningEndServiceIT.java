package com.cinema.catalog.application.services;

import com.cinema.SpringIT;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.ScreeningRepository;
import com.cinema.rooms.application.services.RoomFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.ScreeningTestHelper.createScreening;
import static com.cinema.tickets.TicketTestHelper.createRoomCreateDto;
import static org.assertj.core.api.Assertions.assertThat;

class ScreeningEndServiceIT extends SpringIT {

    @Autowired
    private ScreeningEndService screeningEndService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomFacade roomFacade;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private Clock clockMock;

    @Test
    void shouldRemoveRoomsFromFinishedScreenings() {
        //given
        var film = filmRepository.add(createFilm());
        roomFacade.createRoom(createRoomCreateDto());
        var screeningDate = LocalDateTime.now(clockMock).minusSeconds(1);
        var screening = createScreening(film, screeningDate);
        film.addScreening(screening);
        filmRepository.add(film);

        //when
        screeningEndService.removeRoomsFromEndedScreenings();

        //then
        var screenings = screeningRepository.readAll();
        assertThat(screenings)
                .isNotEmpty()
                .allMatch(it -> it.getRoomId() == null);
    }
}
