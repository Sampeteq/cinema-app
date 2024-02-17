package com.cinema.halls.ui;

import com.cinema.halls.application.HallService;
import com.cinema.halls.domain.Hall;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    @PostMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Hall> createHall(@RequestBody Hall hall) {
        var addedHall = hallService.createHall(hall);
        return ResponseEntity
                .created(URI.create("/admin/halls"))
                .body(addedHall);
    }

    @DeleteMapping("/admin/halls/{id}")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Object> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public List<Hall> getAllHallsWithSeats() {
        return hallService.getAllHallsWithSeats();
    }
}
