package com.cinema.rooms.application.rest;

import com.cinema.SpringIT;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.rooms.RoomFixture.createRoom;

class RoomControllerIT extends SpringIT {

    private static final String ROOMS_ENDPOINT = "/rooms";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @Test
    void rooms_are_read() {
        //given
        var room = roomRepository.add(createRoom());
        var adminMail = "admin@mail.com";
        var adminPassword = "12345";
        var userCreateDto = new UserCreateDto(adminMail, adminPassword);
        userService.createAdmin(userCreateDto);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(userCreateDto.mail(), userCreateDto.password()))
                .exchange();

        //then
        responseSpec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(room.getId())
                .jsonPath("$[0].rowsNumber").isEqualTo(room.getRowsNumber())
                .jsonPath("$[0].rowSeatsNumber").isEqualTo(room.getRowSeatsNumber());
    }

    @Test
    void rooms_are_read_only_by_authorized_user() {
        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .exchange();

        //then
        responseSpec.expectStatus().isUnauthorized();
    }

    @Test
    void rooms_are_read_only_by_admin() {
        //given
        var userMail = "user1@mail.com";
        var userPassword = "12345";
        var userCreateDto = new UserCreateDto(userMail, userPassword);
        userService.createCommonUser(userCreateDto);

        //when
        var responseSpec = webTestClient
                .get()
                .uri(ROOMS_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(userCreateDto.mail(), userCreateDto.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isForbidden();
    }
}
