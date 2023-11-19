package com.cinema.halls;

import com.cinema.halls.domain.Hall;

public class HallFixture {

    private static final String ID = "1";
    private static final int ROWS_NUMBER = 10;
    private static final int ROW_SEATS_NUMBER = 15;

    public static Hall createHall() {
        return new Hall(
                ID,
                ROWS_NUMBER,
                ROW_SEATS_NUMBER
        );
    }
}
