package com.example.screening;

import com.example.screening.exception.WrongScreeningMinAgeException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class MinAge {

    private int value;

    static MinAge of(int value) {
        if (value < ScreeningValues.SCREENING_MIN_AGE || value > ScreeningValues.SCREENING_MAX_AGE) {
            throw new WrongScreeningMinAgeException();
        } else {
            return new MinAge(value);
        }
    }
}
