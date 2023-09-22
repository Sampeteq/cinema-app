package com.cinema.rooms.domain;

import com.cinema.shared.exceptions.EntityNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private int rowsNumber;

    private int rowSeatsNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<RoomOccupation> occupations;

    protected Room() {}

    public Room(String customId, int rowsNumber, int rowSeatsNumber) {
        this.customId = customId;
        this.rowsNumber = rowsNumber;
        this.rowSeatsNumber = rowSeatsNumber;
        this.occupations = new ArrayList<>();
    }

    public void addOccupation(LocalDateTime start, LocalDateTime end) {
        var roomOccupation = new RoomOccupation(start, end);
        this.occupations.add(roomOccupation);
    }

    public void removeOccupation(LocalDateTime start, LocalDateTime end) {
        var foundOccupation = this
                .occupations
                .stream()
                .filter(occupation -> occupation.on(start, end))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Occupation"));
        this.occupations.remove(foundOccupation);
    }

    public boolean hasNoOccupationOn(LocalDateTime start, LocalDateTime end) {
        return this
                .occupations
                .stream()
                .noneMatch(occupation -> occupation.hasCollision(start, end));
    }
}
