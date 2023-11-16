package com.cinema.tickets.ui.rest;

import com.cinema.BaseIT;
import com.cinema.tickets.domain.repositories.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.tickets.SeatFixture.createSeats;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;

class SeatControllerIT extends BaseIT {

    private static final String SEATS_BASE_ENDPOINT = "/seats";

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void seats_are_gotten_by_screening_id() {
        //given
        var screeningId = 1L;
        addSeats(screeningId);

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
                .jsonPath("$.*.status").exists()
                .jsonPath("$.*.*").value(everyItem(notNullValue()));
    }

    private void addSeats(Long screeningId) {
        createSeats(screeningId).forEach(seatRepository::add);
    }
}
