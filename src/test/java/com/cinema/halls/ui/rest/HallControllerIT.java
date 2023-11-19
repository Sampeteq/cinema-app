package com.cinema.halls.ui.rest;

import com.cinema.BaseIT;
import com.cinema.halls.domain.HallRepository;
import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.halls.HallFixture.createHall;

class HallControllerIT extends BaseIT {

    private static final String HALL_ENDPOINT = "/halls";

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Test
    void halls_are_gotten() {
        //given
        var hall = hallRepository.add(createHall());
        var adminMail = "admin@mail.com";
        var adminPassword = "12345";
        var command = new CreateAdmin(adminMail, adminPassword);
        createAdminHandler.handle(command);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(HALL_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(command.adminMail(), command.adminPassword()))
                .exchange();

        //then
        responseSpec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(hall.getId())
                .jsonPath("$[0].rowsNumber").isEqualTo(hall.getRowsNumber())
                .jsonPath("$[0].seatsNumberInOneRow").isEqualTo(hall.getSeatsNumberInOneRow());
    }

    @Test
    void halls_are_gotten_only_by_authorized_user() {
        //when
        var responseSpec = webTestClient
                .get()
                .uri(HALL_ENDPOINT)
                .exchange();

        //then
        responseSpec.expectStatus().isUnauthorized();
    }

    @Test
    void halls_are_gotten_only_by_admin() {
        //given
        var userMail = "user1@mail.com";
        var userPassword = "12345";
        var command = new CreateUser(userMail, userPassword);
        createUserHandler.handle(command);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(HALL_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(command.mail(), command.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isForbidden();
    }
}
