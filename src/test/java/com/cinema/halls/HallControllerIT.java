package com.cinema.halls;

import com.cinema.BaseIT;
import com.cinema.users.User;
import com.cinema.users.UserFixtures;
import com.cinema.users.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.cinema.halls.HallFixtures.createHall;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

class HallControllerIT extends BaseIT {

    private static final String HALL_ADMIN_ENDPOINT = "/admin/halls";

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    void hall_is_created() {
        var hall = new Hall(
                List.of(
                        new Seat(1, 1),
                        new Seat(1, 2)
                )
        );
        var user = addUser();

        webTestClient
                .post()
                .uri(HALL_ADMIN_ENDPOINT)
                .bodyValue(hall)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.seats[0].number").isEqualTo(hall.getSeats().get(0).number())
                .jsonPath("$.seats[0].rowNumber").isEqualTo(hall.getSeats().get(0).rowNumber())
                .jsonPath("$.seats[1].number").isEqualTo(hall.getSeats().get(1).number())
                .jsonPath("$.seats[1].rowNumber").isEqualTo(hall.getSeats().get(1).rowNumber());
    }

    @Test
    void hall_is_deleted() {
        var hall = addHall();
        var user = addUser();

        webTestClient
                .delete()
                .uri(HALL_ADMIN_ENDPOINT + "/" + hall.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus().isNoContent();
        assertThat(hallRepository.findById(hall.getId())).isEmpty();
    }

    @Test
    void halls_with_seats_are_gotten() {
        var hall = hallRepository.save(createHall());
        var user = addUser();

        webTestClient
                .get()
                .uri(HALL_ADMIN_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(hall.getId())
                .jsonPath("$[0].seats").value(hasSize(hall.getSeats().size()));
    }

    private Hall addHall() {
        return hallRepository.save(HallFixtures.createHall());
    }

    private User addUser() {
        return userRepository.save(UserFixtures.createUser(User.Role.ADMIN));
    }
}
