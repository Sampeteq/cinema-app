package com.example.screening;

import com.example.screening.dto.ScreeningRoomDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "SCREENING_ROOMS")
@EqualsAndHashCode(of = "uuid")
@ToString
class ScreeningRoom {

    @Id
    private UUID uuid = UUID.randomUUID();

    private int number;

    private int freeSeats;

    protected ScreeningRoom() {
    }

    ScreeningRoom(int number, int freeSeats) {
        this.number = number;
        this.freeSeats = freeSeats;
    }

    ScreeningRoomDTO toDTO() {
        return new ScreeningRoomDTO(this.uuid, number, freeSeats);
    }
}
