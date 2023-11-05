package com.cinema.screenings.application.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createCreateRoomCommand;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class ReadSeatsControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private CreateRoomHandler createRoomHandler;

    @Test
    void seats_are_read_by_screening_id() {
        //given
        addRoom();
        var screening = addScreening();

        //when
        var spec = webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" +  screening.getId() + "/seats")
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.number").exists()
                .jsonPath("$.*.status").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }

    private void addRoom() {
        createRoomHandler.handle(createCreateRoomCommand());
    }

    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }
}
