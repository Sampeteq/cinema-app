package com.cinema.halls.infrastructure.ui;

import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
