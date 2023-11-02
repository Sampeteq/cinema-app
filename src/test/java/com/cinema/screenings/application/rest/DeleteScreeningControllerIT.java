package com.cinema.screenings.application.rest;

import com.cinema.SpringIT;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cinema.screenings.ScreeningFixture.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixture.createScreening;
import static org.assertj.core.api.Assertions.assertThat;

class DeleteScreeningControllerIT extends SpringIT {

    private static final String SCREENINGS_BASE_ENDPOINT = "/screenings";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "12345";

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Test
    void screening_is_deleted_only_by_admin() {
        //given
        addCommonUser();
        var screeningId = 1L;

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screeningId)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isForbidden();
    }

    @Test
    void screening_is_deleted() {
        //given
        addAdminUser();
        var screening = addScreening();

        //when
        var spec = webTestClient
                .delete()
                .uri(SCREENINGS_BASE_ENDPOINT + "/" + screening.getId())
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange();

        //then
        spec.expectStatus().isNoContent();
        assertThat(screeningRepository.readById(screening.getId())).isEmpty();
    }

    private Screening addScreening() {
        var screening = createScreening(SCREENING_DATE);
        return screeningRepository.add(screening);
    }

    private void addCommonUser() {
        var command = new CreateUser(
                USERNAME,
                PASSWORD
        );
        createUserHandler.handle(command);
    }

    private void addAdminUser() {
        var command = new CreateAdmin(
                USERNAME,
                PASSWORD
        );
        createAdminHandler.handle(command);
    }
}
