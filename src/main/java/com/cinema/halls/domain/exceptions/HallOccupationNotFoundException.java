package com.cinema.halls.domain.exceptions;

public class HallOccupationNotFoundException extends RuntimeException {

    public HallOccupationNotFoundException() {
        super("Hall occupation not found");
    }
}
