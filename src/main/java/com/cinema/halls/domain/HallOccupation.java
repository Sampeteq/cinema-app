package com.cinema.halls.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "halls_occupations")
@Getter
@ToString
public class HallOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @ManyToOne
    private Hall hall;

    private Long screeningId;

    protected HallOccupation() {
    }

    public HallOccupation(LocalDateTime startAt, LocalDateTime endAt, Hall hall, Long screeningId) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.hall = hall;
        this.screeningId = screeningId;
    }

    public boolean hasCollision(LocalDateTime startAt, LocalDateTime endAt) {
        var startCollision = !startAt.isBefore(this.startAt) && !startAt.isAfter(this.endAt);
        var endCollision = !endAt.isBefore(this.startAt) && !endAt.isAfter(this.endAt);
        return startCollision || endCollision;
    }
}
