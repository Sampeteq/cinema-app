package com.cinema.users.ui;

import com.cinema.BaseIT;
import com.cinema.users.UserFixture;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import com.cinema.users.application.commands.handlers.CreateUserHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PermissionsIT extends BaseIT {

    @Autowired
    private CreateAdminHandler createAdminHandler;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Test
    void user_with_admin_role_has_access_to_endpoints_with_admin_prefix() {
        //given
        var command = UserFixture.createCrateUserCommand();
        createAdminHandler.handle(command);
        var sampleAdminEndpoint = "/admin/halls";

        //when
        var responseSpec = webTestClient
                .options()
                .uri(sampleAdminEndpoint)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(command.mail(), command.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isOk();
    }

    @Test
    void user_with_common_role_has_no_access_to_endpoints_with_admin_prefix() {
        //given
        var command = UserFixture.createCrateUserCommand();
        createUserHandler.handle(command);
        var sampleAdminEndpoint = "/admin/halls";

        //when
        var responseSpec = webTestClient
                .options()
                .uri(sampleAdminEndpoint)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(command.mail(), command.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isForbidden();
    }

    @Test
    void user_dont_have_to_be_authenticated_to_have_access_to_endpoint_with_public_prefix() {
        //given
        var samplePublicEndpoint = "/public/films";

        //when
        var responseSpec = webTestClient
                .options()
                .uri(samplePublicEndpoint)
                .exchange();

        //then
        responseSpec.expectStatus().isOk();
    }
}
