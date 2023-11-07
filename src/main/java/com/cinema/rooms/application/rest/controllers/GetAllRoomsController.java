package com.cinema.rooms.application.rest.controllers;

import com.cinema.rooms.application.queries.GetAllRooms;
import com.cinema.rooms.application.queries.dto.RoomDto;
import com.cinema.rooms.application.queries.handlers.GetAllRoomsHandler;
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
public class GetAllRoomsController {

    private final GetAllRoomsHandler getAllRoomsHandler;

    @GetMapping
    @SecurityRequirement(name = "basic")
    public List<RoomDto> getAllRooms() {
        var query = new GetAllRooms();
        log.info("Query:{}", query);
        return getAllRoomsHandler.handle(query);
    }
}
