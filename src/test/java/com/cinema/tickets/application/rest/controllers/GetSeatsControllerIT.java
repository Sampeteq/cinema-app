package com.cinema.tickets.application.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.rooms.application.commands.handlers.CreateRoomHandler;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.tickets.domain.repositories.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createCreateRoomCommand;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static com.cinema.tickets.SeatFixture.createSeats;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class GetSeatsControllerIT extends SpringIT {

    private static final String SEATS_BASE_ENDPOINT = "/seats";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CreateRoomHandler createRoomHandler;

    @Test
    void seats_are_gotten_by_screening_id() {
        //given
        addRoom();
        var screening = addScreening();
        addSeats(screening.getId());

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEATS_BASE_ENDPOINT)
                        .queryParam("screeningId", screening.getId())
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
                .jsonPath("$.*.status").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }

    private void addSeats(Long screeningId) {
        createSeats(screeningId).forEach(seatRepository::add);
    }

    private void addRoom() {
        createRoomHandler.handle(createCreateRoomCommand());
    }

    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }
}
