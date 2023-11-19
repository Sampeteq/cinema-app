package com.cinema.halls.domain;

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
@Table(name = "halls")
@Getter
public class Hall {

    @Id
    private String id;

    private int rowsNumber;

    private int seatsNumberInOneRow;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hall_id")
    private List<HallOccupation> occupations;

    protected Hall() {}

    public Hall(String id, int rowsNumber, int seatsNumberInOneRow) {
        this.id = id;
        this.rowsNumber = rowsNumber;
        this.seatsNumberInOneRow = seatsNumberInOneRow;
        this.occupations = new ArrayList<>();
    }

    public HallOccupation addOccupation(LocalDateTime start, LocalDateTime end) {
        var hallOccupation = new HallOccupation(start, end);
        this.occupations.add(hallOccupation);
        return hallOccupation;
    }

    public boolean hasNoOccupationOn(LocalDateTime start, LocalDateTime end) {
        return this
                .occupations
                .stream()
                .noneMatch(occupation -> occupation.hasCollision(start, end));
    }
}
