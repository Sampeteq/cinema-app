package com.cinema.users;

import com.cinema.BaseIT;
import com.cinema.users.UserFixtures;
import com.cinema.users.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PermissionsIT extends BaseIT {

    @Autowired
    private UserService userService;

    @Test
    void user_with_admin_role_has_access_to_endpoints_with_admin_prefix() {
        //given
        var userCreateRequest = UserFixtures.createUserCreateRequest();
        userService.createAdmin(userCreateRequest.mail(), userCreateRequest.password());
        var sampleAdminEndpoint = "/admin/halls";

        //when
        var responseSpec = webTestClient
                .options()
                .uri(sampleAdminEndpoint)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userCreateRequest.mail(), userCreateRequest.password()))
                .exchange();

        //then
        responseSpec.expectStatus().isOk();
    }

    @Test
    void user_with_common_role_has_no_access_to_endpoints_with_admin_prefix() {
        //given
        var userCreateRequest = UserFixtures.createUserCreateRequest();
        userService.createUser(userCreateRequest.mail(), userCreateRequest.password());
        var sampleAdminEndpoint = "/admin/halls";

        //when
        var responseSpec = webTestClient
                .options()
                .uri(sampleAdminEndpoint)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(userCreateRequest.mail(), userCreateRequest.password()))
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
