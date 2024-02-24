package com.cinema.halls;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@ToString(exclude = "seats")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "seat")
    private List<Seat> seats;

    protected Hall() {}

    public Hall(List<Seat> seats) {
        this.seats = seats;
    }
}
