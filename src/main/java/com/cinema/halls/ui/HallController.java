package com.cinema.halls.ui;

import com.cinema.halls.application.commands.CreateHall;
import com.cinema.halls.application.commands.DeleteHall;
import com.cinema.halls.application.commands.handlers.CreateHallHandler;
import com.cinema.halls.application.commands.handlers.DeleteHallHandler;
import com.cinema.halls.application.queries.GetAllHalls;
import com.cinema.halls.application.queries.handlers.GetAllHallsHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HallController {

    private final CreateHallHandler createHallHandler;
    private final DeleteHallHandler deleteHallHandler;
    private final GetAllHallsHandler getAllHallsHandler;

    @PostMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Object> createHall(@RequestBody CreateHall command) {
        log.info("Command:{}", command);
        createHallHandler.handle(command);
        var responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.info("Response:{}", responseEntity);
        return responseEntity;
    }

    @DeleteMapping("/admin/halls/{id}")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Object> deleteHall(@PathVariable Long id) {
        var command = new DeleteHall(id);
        log.info("Command:{}", command);
        deleteHallHandler.handle(command);
        var responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        log.info("Response:{}", responseEntity);
        return responseEntity;
    }

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public HallsResponse getAllHalls() {
        var query = new GetAllHalls();
        log.info("Query:{}", query);
        var halls =  getAllHallsHandler.handle(query);
        return new HallsResponse(halls);
    }
}
