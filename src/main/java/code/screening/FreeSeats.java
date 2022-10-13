package code.screening;

import code.screening.exception.ScreeningFreeSeatsQuantityException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static code.screening.ScreeningValues.SCREENING_MAX_FREE_SEATS;
import static code.screening.ScreeningValues.SCREENING_MIN_FREE_SEATS;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class FreeSeats {

    private int value;

    static FreeSeats of(int value) {
        if (value == 0) {
            return new FreeSeats(value);
        } else if (value < SCREENING_MIN_FREE_SEATS || value > SCREENING_MAX_FREE_SEATS) {
            throw new ScreeningFreeSeatsQuantityException();
        } else {
            return new FreeSeats(value);
        }
    }
}
