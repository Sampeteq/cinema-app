package com.cinema.tickets.ui;

import com.cinema.BaseIT;
import com.cinema.films.FilmFixtures;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.tickets.TicketFixtures;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.users.UserFixtures;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.cinema.screenings.ScreeningFixtures.createScreening;

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

    protected Ticket addTicket(Screening screening) {
        return ticketRepository.save(TicketFixtures.createTicket(screening));
    }

    protected Ticket addTicket(Screening screening, Long userId) {
        return ticketRepository.save(TicketFixtures.createTicket(screening, userId));
    }

    protected List<Ticket> addTickets(Screening screening) {
        return ticketRepository.saveAll(
                List.of(
                        new Ticket(screening, screening.getHall().getSeats().get(0)),
                        new Ticket(screening, screening.getHall().getSeats().get(1))
                )
        );
    }

    protected Screening addScreening(Film film, Hall hall) {
        return screeningRepository.save(createScreening(film, hall));
    }

    protected Screening addScreening(LocalDateTime date, Film film, Hall hall) {
        return screeningRepository.save(createScreening(date, film, hall));
    }

    protected Hall addHall() {
        return hallRepository.save(HallFixtures.createHall());
    }

    protected Film addFilm() {
        return filmRepository.save(FilmFixtures.createFilm());
    }

    protected User addUser() {
        return userRepository.save(UserFixtures.createUser());
    }

    protected User addAdmin() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }

    protected List<User> addUsers() {
        return Stream
                .of("user1@mail.com", "user2@mail.com", "user3@mail.com")
                .map(mail -> userRepository.save(UserFixtures.createUser(mail, UserFixtures.PASSWORD)))
                .toList();
    }
}
