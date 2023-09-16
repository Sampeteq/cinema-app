package com.cinema.catalog.application.services;

import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.ports.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class RoomAvailableService {

    private final RoomRepository roomRepository;

    Optional<Room> findAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return roomRepository.readFirstAvailableRoom(start, end);
    }
}
