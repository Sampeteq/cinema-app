package com.cinema.rooms.application.rest;

import com.cinema.rooms.application.dto.RoomDto;
import com.cinema.rooms.application.services.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @SecurityRequirement(name = "basic")
    public List<RoomDto> readAllRooms() {
        return roomService.readAllRooms();
    }
}
