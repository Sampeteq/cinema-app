package com.cinema.screenings.application.rest.controllers;

import com.cinema.SpringIT;
import com.cinema.films.application.commands.handlers.CreateFilmHandler;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createCreateFilmCommand;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class ReadScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private CreateFilmHandler createFilmHandler;

    @Test
    void screenings_are_read() {
        //given
        var filmTitle = "Sample title";
        addFilm(filmTitle);
        var screening = addScreening();

        //when
        var spec = webTestClient
                .get()
                .uri(SCREENINGS_BASE_ENDPOINT)
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[*]").value(hasSize(1))
                .jsonPath("$[*].*").value(everyItem(notNullValue()))
                .jsonPath("$[0].date").isEqualTo(screening.getDate().toString())
                .jsonPath("$[0].filmTitle").isEqualTo(filmTitle);
    }

    @Test
    void screenings_are_read_by_date() {
        //given
        addFilm();
        var requiredDate = LocalDate.of(2023, 12, 13);
        var screeningWithRequiredDate = addScreening(requiredDate);
        addScreening(requiredDate.minusDays(1));

        //when
        var spec = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(SCREENINGS_BASE_ENDPOINT)
                        .queryParam("date", requiredDate.toString())
                        .build()
                )
                .exchange();

        //then
        spec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.[*].date").isEqualTo(screeningWithRequiredDate.getDate().toString());
    }

    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }

    private Screening addScreening(LocalDate date) {
        var dateTime = date.atStartOfDay().plusHours(16);
        var screening = createScreening(dateTime);
        return screeningRepository.add(screening);
    }

    private void addFilm() {
        createFilmHandler.handle(createCreateFilmCommand());
    }

    private void addFilm(String title) {
        createFilmHandler.handle(createCreateFilmCommand(title));
    }
}
