package com.cinema.halls.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@ToString(exclude = "seats")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hall_id")
    private List<Seat> seats;

    protected Hall() {}

    public Hall(List<Seat> seats) {
        this.seats = seats;
    }
}
