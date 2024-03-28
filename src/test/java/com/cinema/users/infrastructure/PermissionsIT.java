package com.cinema.users.infrastructure;

import com.cinema.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class PermissionsIT extends BaseIT {

    @Test
    @WithMockUser(authorities = "ADMIN")
    void user_with_admin_role_has_access_to_endpoints_with_admin_prefix() {
        webTestClient
                .options()
                .uri("/admin/test")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @WithMockUser(authorities = "COMMON")
    void user_with_common_role_has_no_access_to_endpoints_with_admin_prefix() {
        webTestClient
                .options()
                .uri("/admin/test")
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
