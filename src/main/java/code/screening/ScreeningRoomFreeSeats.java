package code.screening;

import code.screening.exception.ScreeningFreeSeatsQuantityException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class ScreeningRoomFreeSeats {

    private int value;

    private static final int SCREENING_MIN_FREE_SEATS = 50;

    private static final int SCREENING_MAX_FREE_SEATS = 200;

    static ScreeningRoomFreeSeats of(int value) {
        if (value == 0) {
            return new ScreeningRoomFreeSeats(value);
        } else if (value < SCREENING_MIN_FREE_SEATS || value > SCREENING_MAX_FREE_SEATS) {
            throw new ScreeningFreeSeatsQuantityException(SCREENING_MIN_FREE_SEATS, SCREENING_MAX_FREE_SEATS);
        } else {
            return new ScreeningRoomFreeSeats(value);
        }
    }

    boolean anyFree() {
        return this.value > 0;
    }

    public int currentFree() {
        return this.value;
    }
}
