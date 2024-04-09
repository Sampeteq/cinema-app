package com.cinema.halls.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString(exclude = "seats")
public class Hall {
    private UUID id;
    private List<Seat> seats;
}
