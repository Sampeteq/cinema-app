package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixture;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixture;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
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

    protected Ticket addTicket(Screening screening, User user) {
        return ticketRepository.save(TicketFixture.createTicket(screening, user));
    }

    protected Screening addScreening(Film film, Hall hall) {
        return screeningRepository.save(ScreeningFixture.createScreening(film, hall));
    }

    protected Screening addScreeningWithTicket(Film film, Hall hall) {
        return screeningRepository.save(ScreeningFixture.createScreeningWithTicket(film, hall));
    }

    protected Screening addScreeningWithTickets(Film film, Hall hall) {
        return screeningRepository.save(ScreeningFixture.createScreeningWithTickets(film, hall));
    }

    protected Screening addScreeningWithTickets(LocalDateTime date, Film film, Hall hall) {
        return screeningRepository.save(createScreeningWithTickets(date, film, hall));
    }

    protected Screening addScreeningWithBookedTicket(Film film, Hall hall, User user) {
        return screeningRepository.save(createScreeningWithBookedTicket(film, hall, user));
    }

    protected Hall addHall() {
        return hallRepository.save(HallFixture.createHall());
    }

    protected Film addFilm() {
        return filmRepository.save(FilmFixture.createFilm());
    }

    protected List<User> addUsers() {
        return Stream
                .of("user1@mail.com", "user2@mail.com", "user3@mail.com")
                .map(mail -> userRepository.save(UserFixture.createUser(mail, UserFixture.PASSWORD)))
                .toList();
    }
}
