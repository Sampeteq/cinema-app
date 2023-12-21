package com.cinema.halls.ui;

import com.cinema.BaseIT;
import com.cinema.halls.domain.HallOccupation;
import com.cinema.halls.domain.HallRepository;
import com.cinema.users.UserFixture;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.cinema.halls.HallFixture.createHall;
import static com.cinema.users.UserFixture.createCrateUserCommand;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class HallControllerIT extends BaseIT {

    private static final String HALL_ADMIN_ENDPOINT = "/admin/halls";

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Test
    void halls_are_gotten() {
        //given
        var hall = hallRepository.add(createHall());
        var crateUserCommand = createCrateUserCommand();
        createAdminHandler.handle(crateUserCommand);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(HALL_ADMIN_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(crateUserCommand.mail(), crateUserCommand.password()))
                .exchange();

        //then
        responseSpec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.halls[0].id").isEqualTo(hall.getId())
                .jsonPath("$.halls[0].seats").value(hasSize(hall.getSeats().size()));
    }

    @Test
    void hall_occupations_are_gotten() {
        //given
        var screeningId = 1L;
        var hallId = 1L;
        var hallOccupation = addHallOccupation(screeningId);
        var crateUserCommand = UserFixture.createCrateUserCommand();
        createAdminHandler.handle(crateUserCommand);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(HALL_ADMIN_ENDPOINT)
                        .pathSegment("occupations")
                        .build()
                )
                .headers(headers -> headers.setBasicAuth(crateUserCommand.mail(), crateUserCommand.password()))
                .exchange();

        //then
        responseSpec
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$.hallsOccupations[0].startAt").isEqualTo(hallOccupation.getStartAt().toString())
                .jsonPath("$.hallsOccupations[0].endAt").isEqualTo(hallOccupation.getEndAt().toString())
                .jsonPath("$.hallsOccupations[0].hallId").isEqualTo(hallId)
                .jsonPath("$.hallsOccupations[0].screeningId").isEqualTo(screeningId);
    }

    private HallOccupation addHallOccupation(Long screeningId) {
        var hall = createHall();
        var date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        var hallOccupation = hall.addOccupation(date, date.plusHours(2), screeningId);
        hallRepository.add(hall);
        return hallOccupation;
    }
}
