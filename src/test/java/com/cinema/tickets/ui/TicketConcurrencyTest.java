package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixture;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.HallSeat;
import com.cinema.screenings.ScreeningFixture;
import com.cinema.screenings.ScreeningSeatFixture;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.ScreeningSeat;
import com.cinema.screenings.domain.ScreeningSeatRepository;
import com.cinema.tickets.application.dto.BookTicketDto;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

class TicketConcurrencyTest extends BaseIT {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void ticket_is_booked_only_by_one_user() {
        //given
        var film = addFilm();
        var hall = addHall();
        var screening = addScreening(film, hall);
        var seat = addSeat(screening, hall.getSeats().getFirst());
        var users = addUsers();
        var bookTicketDto = new BookTicketDto(screening.getId(), List.of(seat.getId()));

        //when
        try (var executorService = Executors.newFixedThreadPool(3)) {
            executorService.submit(() -> bookTicket(users.getFirst().getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(1).getMail(), bookTicketDto));
            executorService.submit(() -> bookTicket(users.get(2).getMail(), bookTicketDto));
            executorService.shutdown();
        }

        //then
        Assertions.assertThat(ticketRepository.getAll()).hasSize(1);
    }

    private void bookTicket(String userMail, BookTicketDto bookTicketDto) {
        webTestClient
                .post()
                .uri("/tickets")
                .headers(headers -> headers.setBasicAuth(userMail, UserFixture.PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookTicketDto)
                .exchange();
    }

    private Screening addScreening(Film film, Hall hall) {
        return screeningRepository.add(ScreeningFixture.createScreening(film, hall));
    }

    private ScreeningSeat addSeat(Screening screening, HallSeat hallSeat) {
        return screeningSeatRepository.add(ScreeningSeatFixture.createSeat(screening, hallSeat));
    }

    private Hall addHall() {
        return hallRepository.add(HallFixture.createHall());
    }

    private Film addFilm() {
        return filmRepository.add(FilmFixture.createFilm());
    }

    private List<User> addUsers() {
        return Stream
                .of("user1@mail.com", "user2@mail.com", "user3@mail.com")
                .map(mail -> userRepository.add(UserFixture.createUser(mail, UserFixture.PASSWORD)))
                .toList();
    }
}
