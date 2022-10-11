package com.example.screening.dto;

import java.util.UUID;

public record ScreeningRoomDTO(UUID uuid, int number, int freeSeats) {
}
