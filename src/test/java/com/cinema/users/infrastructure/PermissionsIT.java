package com.cinema.users.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import(UserSecurityConfig.class)
class PermissionsIT  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void user_with_admin_role_has_access_to_admin_endpoints() throws Exception {
        mockMvc
                .perform(get("/admin/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "COMMON")
    void user_with_common_role_has_no_access_to_admin_endpoints() throws Exception {
        mockMvc
                .perform(get("/admin/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    void user_have_access_to_public_endpoints() throws Exception {
        mockMvc
                .perform(get("/public/test"))
                .andExpect(status().isOk());
    }
}

@RestController
class TestController {

    @GetMapping("/admin/test")
    void adminEndpoint() {}

    @GetMapping("/public/test")
    void publicEndpoint() {}
}
