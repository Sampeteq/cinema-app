package com.cinema.halls.ui;

import com.cinema.halls.application.queries.GetAllHalls;
import com.cinema.halls.application.queries.handlers.GetAllHallsHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HallController {

    private final GetAllHallsHandler getAllHallsHandler;

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public HallsResponse getAllHalls() {
        var query = new GetAllHalls();
        log.info("Query:{}", query);
        var halls =  getAllHallsHandler.handle(query);
        return new HallsResponse(halls);
    }
}
