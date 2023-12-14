package com.cinema.halls.ui;

import com.cinema.halls.application.queries.GetAllHallOccupations;
import com.cinema.halls.application.queries.GetAllHalls;
import com.cinema.halls.application.queries.dto.HallDto;
import com.cinema.halls.application.queries.dto.HallOccupationDto;
import com.cinema.halls.application.queries.handlers.GetAllHallOccupationsHandler;
import com.cinema.halls.application.queries.handlers.GetAllHallsHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HallController {

    private final GetAllHallsHandler getAllHallsHandler;
    private final GetAllHallOccupationsHandler getAllHallOccupationsHandler;

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public List<HallDto> getAllHalls() {
        var query = new GetAllHalls();
        log.info("Query:{}", query);
        return getAllHallsHandler.handle(query);
    }

    @GetMapping("/admin/halls/occupations")
    @SecurityRequirement(name = "basic")
    List<HallOccupationDto> getAllOccupations() {
        var query = new GetAllHallOccupations();
        log.error("Query:{}", query);
        return getAllHallOccupationsHandler.handle(query);
    }
}
