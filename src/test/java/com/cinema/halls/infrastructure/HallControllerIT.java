package com.cinema.halls.infrastructure;

import com.cinema.BaseIT;
import com.cinema.halls.HallFixtures;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.Seat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.cinema.halls.HallFixtures.createHall;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

class HallControllerIT extends BaseIT {

    private static final String HALL_ADMIN_ENDPOINT = "/admin/halls";

    @Autowired
    private HallRepository hallRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void hall_is_created() {
        var hall = new Hall(
                List.of(
                        new Seat(1, 1),
                        new Seat(1, 2)
                )
        );

        webTestClient
                .post()
                .uri(HALL_ADMIN_ENDPOINT)
                .bodyValue(hall)
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
    @WithMockUser(authorities = "ADMIN")
    void hall_is_deleted() {
        var hall = addHall();

        webTestClient
                .delete()
                .uri(HALL_ADMIN_ENDPOINT + "/" + hall.getId())
                .exchange()
                .expectStatus().isNoContent();

        assertThat(hallRepository.getById(hall.getId())).isEmpty();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void halls_with_seats_are_gotten() {
        var hall = hallRepository.save(createHall());

        webTestClient
                .get()
                .uri(HALL_ADMIN_ENDPOINT)
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
}
