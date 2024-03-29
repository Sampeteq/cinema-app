package com.cinema.users.infrastructure;

import com.cinema.BaseIT;
import com.cinema.users.domain.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cinema.users.UserFixtures.MAIL;
import static com.cinema.users.UserFixtures.PASSWORD;

class PermissionsIT extends BaseIT {

    @Autowired
    private UserService userService;

    @Test
    void user_with_admin_role_has_access_to_endpoints_with_admin_prefix() {
        userService.createAdmin(MAIL, PASSWORD);

        webTestClient
                .options()
                .uri("/admin/test")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(MAIL, PASSWORD))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void user_with_common_role_has_no_access_to_endpoints_with_admin_prefix() {
        userService.createUser(MAIL, PASSWORD);

        webTestClient
                .options()
                .uri("/admin/test")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(MAIL, PASSWORD))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void user_dont_have_to_be_authenticated_to_have_access_to_endpoint_with_public_prefix() {
        webTestClient
                .options()
                .uri("/public/films")
                .exchange()
                .expectStatus()
                .isOk();
    }
}

@RestController
class TestController {

    @GetMapping("/admin/test")
    void adminEndpoint() {}

    @GetMapping("/public/test")
    void publicEndpoint() {}
}
