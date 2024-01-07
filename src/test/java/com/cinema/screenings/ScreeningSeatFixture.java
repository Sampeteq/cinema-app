package com.cinema.screenings;

import com.cinema.halls.domain.HallSeat;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningSeat;

import java.util.List;

public final class ScreeningSeatFixture {
    public static final boolean IS_FREE = true;

    private ScreeningSeatFixture() {
    }

    public static ScreeningSeat createSeat(Screening screening, HallSeat hallSeat) {
        return new ScreeningSeat(IS_FREE, hallSeat, screening);
    }

    public static ScreeningSeat createNotFreeSeat(Screening screening, HallSeat hallSeat) {
        return new ScreeningSeat(false, hallSeat, screening);
    }

    public static List<ScreeningSeat> createSeats(Screening screening, List<HallSeat> hallSeats) {
        return hallSeats
                .stream()
                .map(hallSeat -> new ScreeningSeat(IS_FREE, hallSeat, screening))
                .toList();
    }
}
