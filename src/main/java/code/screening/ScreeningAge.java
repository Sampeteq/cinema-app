package code.screening;

import code.screening.exception.ScreeningAgeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class ScreeningAge {

    private int value;

    private static final int SCREENING_MIN_AGE = 5;

    private static final int SCREENING_MAX_AGE = 21;

    static ScreeningAge of(int value) {
        if (value < SCREENING_MIN_AGE || value > SCREENING_MAX_AGE) {
            throw new ScreeningAgeException(SCREENING_MIN_AGE, SCREENING_MAX_AGE);
        } else {
            return new ScreeningAge(value);
        }
    }
}
