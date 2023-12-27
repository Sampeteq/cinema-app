package com.cinema.halls.ui;

import com.cinema.BaseIT;
import com.cinema.halls.domain.HallRepository;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.halls.HallFixture.createHall;
import static com.cinema.users.UserFixture.createCrateUserCommand;
import static org.hamcrest.Matchers.hasSize;

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
}
