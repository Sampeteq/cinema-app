package com.cinema.tickets;

import com.cinema.BaseIT;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.Hall;
import com.cinema.halls.HallRepository;
import com.cinema.halls.Seat;
import com.cinema.screenings.Screening;
import com.cinema.screenings.ScreeningRepository;
import com.cinema.tickets.TicketFixtures;
import com.cinema.tickets.Ticket;
import com.cinema.tickets.TicketRepository;
import com.cinema.users.UserFixtures;
import com.cinema.users.User;
import com.cinema.users.UserRepository;
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
                        new Ticket(screening, new Seat(1, 1)),
                        new Ticket(screening, new Seat(1, 2))
                )
        );
    }

    protected Screening addScreening() {
        return screeningRepository.save(createScreening());
    }

    protected Screening addScreening(LocalDateTime date) {
        return screeningRepository.save(createScreening(date));
    }

    protected Screening addScreening(Long hallId) {
        return screeningRepository.save(createScreening(hallId));
    }

    protected Hall addHall() {
        return hallRepository.save(HallFixtures.createHall());
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
