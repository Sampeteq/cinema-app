package com.cinema.rooms.application.rest.controllers;

import com.cinema.rooms.application.queries.ReadAllRooms;
import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.handlers.ReadAllRoomsHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@Tag(name = "rooms")
@Slf4j
public class ReadAllRoomsController {

    private final ReadAllRoomsHandler readAllRoomsHandler;

    @GetMapping
    @SecurityRequirement(name = "basic")
    public List<RoomDto> readAllRooms() {
        var query = new ReadAllRooms();
        log.info("Query:{}", query);
        return readAllRoomsHandler.handle(query);
    }
}
