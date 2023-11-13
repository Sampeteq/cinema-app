package com.cinema.rooms.ui.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.rooms.RoomFixture.createRoom;

class GetAllRoomsControllerIT extends SpringIT {

    private static final String ROOMS_ENDPOINT = "/rooms";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Test
    void rooms_are_gotten() {
        //given
        var room = roomRepository.add(createRoom());
        var adminMail = "admin@mail.com";
        var adminPassword = "12345";
        var command = new CreateAdmin(adminMail, adminPassword);
        createAdminHandler.handle(command);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(command.adminMail(), command.adminPassword()))
                .exchange();

        //then
        responseSpec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(room.getId())
                .jsonPath("$[0].rowsNumber").isEqualTo(room.getRowsNumber())
                .jsonPath("$[0].seatsNumberInOneRow").isEqualTo(room.getSeatsNumberInOneRow());
    }

    @Test
    void rooms_are_gotten_only_by_authorized_user() {
        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .exchange();

        //then
        responseSpec.expectStatus().isUnauthorized();
    }

    @Test
    void rooms_are_gotten_only_by_admin() {
        //given
        var userMail = "user1@mail.com";
        var userPassword = "12345";
        var command = new CreateUser(userMail, userPassword);
        createUserHandler.handle(command);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(command.mail(), command.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isForbidden();
    }
}
