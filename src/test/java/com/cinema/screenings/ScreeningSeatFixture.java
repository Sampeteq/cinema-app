package com.cinema.screenings;

import com.cinema.screenings.domain.ScreeningSeat;

import java.util.List;

public final class ScreeningSeatFixture {

    public static final int ROW_NUMBER = 1;
    public static final int NUMBER = 1;
    public static final boolean IS_FREE = true;

    private ScreeningSeatFixture() {
    }

    public static List<ScreeningSeat> createSeats(Long screeningId) {
        return List.of(
                new ScreeningSeat(ROW_NUMBER, NUMBER, IS_FREE, screeningId),
                new ScreeningSeat(ROW_NUMBER + 1, NUMBER + 1, IS_FREE, screeningId)
        );
    }
}
