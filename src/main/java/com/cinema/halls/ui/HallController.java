package com.cinema.halls.ui;

import com.cinema.halls.application.HallService;
import com.cinema.halls.application.dto.CreateHallDto;
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

@RestController
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    @PostMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Object> createHall(@RequestBody CreateHallDto dto) {
        hallService.createHall(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/halls/{id}")
    @SecurityRequirement(name = "basic")
    public ResponseEntity<Object> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/halls")
    @SecurityRequirement(name = "basic")
    public HallsResponse getAllHalls() {
        var halls =  hallService.getAllHalls();
        return new HallsResponse(halls);
    }
}
