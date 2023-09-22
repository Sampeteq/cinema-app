package com.cinema.rooms.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms_occupations")
@Getter
@ToString
public class RoomOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    protected RoomOccupation() {
    }

    public RoomOccupation(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public boolean hasCollision(LocalDateTime startAt, LocalDateTime endAt) {
        var startCollision = !startAt.isBefore(this.startAt) && !startAt.isAfter(this.endAt);
        var endCollision = !endAt.isBefore(this.startAt) && !endAt.isAfter(this.endAt);
        return startCollision || endCollision;
    }
}
