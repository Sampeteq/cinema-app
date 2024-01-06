package com.cinema.screenings;

import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningSeat;

import java.util.List;

public final class ScreeningSeatFixture {

    public static final int ROW_NUMBER = 1;
    public static final int NUMBER = 1;
    public static final boolean IS_FREE = true;

    private ScreeningSeatFixture() {
    }

    public static ScreeningSeat createSeat(Screening screening) {
        return new ScreeningSeat(ROW_NUMBER, NUMBER, IS_FREE, screening);
    }

    public static ScreeningSeat createNotFreeSeat(Screening screening) {
        return new ScreeningSeat(ROW_NUMBER, NUMBER, false, screening);
    }

    public static List<ScreeningSeat> createSeats(Screening screening) {
        return List.of(
                new ScreeningSeat(ROW_NUMBER, NUMBER, IS_FREE, screening),
                new ScreeningSeat(ROW_NUMBER + 1, NUMBER + 1, IS_FREE, screening)
        );
    }
}
