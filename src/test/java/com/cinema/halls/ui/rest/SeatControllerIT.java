package com.cinema.halls.ui.rest;

import com.cinema.BaseIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.halls.infrastructure.config.CreateHallService;
import com.cinema.screenings.application.commands.handlers.CreateScreeningHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.films.FilmFixture.createCreateFilmCommand;
import static com.cinema.halls.HallFixture.createCreateHallCommand;
import static com.cinema.screenings.ScreeningFixture.createCreateScreeningCommand;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class SeatControllerIT extends BaseIT {

    private static final String SEATS_BASE_ENDPOINT = "/seats";

    @Autowired
    private CreateHallService createHallService;

    @Autowired
    private CreateFilmHandler createFilmHandler;

    @Autowired
    private CreateScreeningHandler createScreeningHandler;

    @Test
    void seats_are_gotten_by_screening_id() {
        //given
        createFilmHandler.handle(createCreateFilmCommand());
        createHallService.handle(createCreateHallCommand());
        createScreeningHandler.handle(createCreateScreeningCommand());
        var screeningId = 1L;

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEATS_BASE_ENDPOINT)
                        .queryParam("screeningId", screeningId)
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.*.rowNumber").exists()
                .jsonPath("$.*.number").exists()
                .jsonPath("$.*.isFree").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }
}
