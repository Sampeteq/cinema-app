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
import com.cinema.tickets.TicketFixture;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.cinema.tickets.TicketFixture.createCancelledTicket;

abstract class TicketBaseIT extends BaseIT {
    protected static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    protected TicketRepository ticketRepository;

    @Autowired
    protected ScreeningRepository screeningRepository;

    @Autowired
    protected ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    protected HallRepository hallRepository;

    @Autowired
    protected FilmRepository filmRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected Clock clock;

    protected Ticket addTicket(ScreeningSeat seat, User user) {
        return ticketRepository.add(TicketFixture.createTicket(seat, user));
    }

    protected Ticket addCancelledTicket(ScreeningSeat seat, User user) {
        return ticketRepository.add(createCancelledTicket(seat, user));
    }

    protected Screening addScreening(Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreening(film, hall));
    }

    protected Screening addScreening(LocalDateTime date, Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreening(date, film, hall));
    }

    protected ScreeningSeat addSeat(Screening screening, HallSeat hallSeat) {
        return screeningSeatRepository.add(ScreeningSeatFixture.createSeat(screening, hallSeat));
    }

    protected ScreeningSeat addNotFreeSeat(Screening screening, HallSeat hallSeat) {
        return screeningSeatRepository.add(ScreeningSeatFixture.createNotFreeSeat(screening, hallSeat));
    }

    protected Hall addHall() {
        return hallRepository.add(HallFixture.createHall());
    }

    protected Film addFilm() {
        return filmRepository.add(FilmFixture.createFilm());
    }

    protected List<User> addUsers() {
        return Stream
                .of("user1@mail.com", "user2@mail.com", "user3@mail.com")
                .map(mail -> userRepository.add(UserFixture.createUser(mail, UserFixture.PASSWORD)))
                .toList();
    }
}
