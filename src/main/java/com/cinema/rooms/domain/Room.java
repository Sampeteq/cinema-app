package com.cinema.rooms.domain;

import com.cinema.rooms.domain.exceptions.RoomOccupationNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
public class Room {

    @Id
    private String id;

    private int rowsNumber;

    private int rowSeatsNumber;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<RoomOccupation> occupations;

    protected Room() {}

    public Room(String id, int rowsNumber, int rowSeatsNumber) {
        this.id = id;
        this.rowsNumber = rowsNumber;
        this.rowSeatsNumber = rowSeatsNumber;
        this.occupations = new ArrayList<>();
    }

    public void addOccupation(LocalDateTime start, LocalDateTime end) {
        var roomOccupation = new RoomOccupation(start, end);
        this.occupations.add(roomOccupation);
    }

    public void removeOccupation(LocalDateTime start) {
        var foundOccupation = this
                .occupations
                .stream()
                .filter(occupation -> occupation.on(start))
                .findFirst()
                .orElseThrow(RoomOccupationNotFoundException::new);
        this.occupations.remove(foundOccupation);
    }

    public boolean hasNoOccupationOn(LocalDateTime start, LocalDateTime end) {
        return this
                .occupations
                .stream()
                .noneMatch(occupation -> occupation.hasCollision(start, end));
    }
}
