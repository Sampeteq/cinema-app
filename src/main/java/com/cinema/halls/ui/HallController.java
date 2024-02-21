package com.cinema.halls.ui;

import com.cinema.halls.application.HallService;
import com.cinema.halls.domain.Hall;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
class HallController {

    private final HallService hallService;

    @PostMapping("/admin/halls")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "basic")
    Hall createHall(@RequestBody Hall hall) {
        return hallService.createHall(hall);
    }

    @DeleteMapping("/admin/halls/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "basic")
    void deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
    }

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    List<Hall> getAllHallsWithSeats() {
        return hallService.getAllHallsWithSeats();
    }
}
