package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixture;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.Seat;
import com.cinema.screenings.ScreeningFixture;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
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

import static com.cinema.screenings.ScreeningFixture.createScreeningWithBookedTicket;
import static com.cinema.screenings.ScreeningFixture.createScreeningWithTickets;
import static com.cinema.tickets.TicketFixture.createCancelledTicket;

abstract class TicketBaseIT extends BaseIT {
    protected static final String TICKETS_BASE_ENDPOINT = "/tickets";

    @Autowired
    protected TicketRepository ticketRepository;

    @Autowired
    protected ScreeningRepository screeningRepository;

    @Autowired
    protected HallRepository hallRepository;

    @Autowired
    protected FilmRepository filmRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected Clock clock;

    protected Ticket addTicket(Screening screening, Seat seat, User user) {
        return ticketRepository.add(TicketFixture.createTicket(screening, seat, user));
    }

    protected Ticket addCancelledTicket(Screening screening, Seat seat, User user) {
        return ticketRepository.add(createCancelledTicket(screening, seat, user));
    }

    protected Screening addScreening(Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreening(film, hall));
    }

    protected Screening addScreeningWithTicket(Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreeningWithTicket(hall, film));
    }

    protected Screening addScreeningWithTickets(Hall hall, Film film) {
        return screeningRepository.add(ScreeningFixture.createScreeningWithTickets(hall, film));
    }

    protected Screening addScreeningWithTickets(LocalDateTime date, Hall hall, Film film) {
        return screeningRepository.add(createScreeningWithTickets(date, film, hall));
    }

    protected Screening addScreeningWithBookedFreeSeat(Hall hall, Film film, User user) {
        return screeningRepository.add(createScreeningWithBookedTicket(hall, film, user));
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
