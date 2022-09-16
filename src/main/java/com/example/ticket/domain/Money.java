package com.example.ticket.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
class Money {

    private BigDecimal value;

    static Money of(BigDecimal value) {
        return new Money(value);
    }

    Money subtractPercent(Double percent) {
        if (percent < 0 || percent > 1) {
            throw new IllegalArgumentException("A percent cannot be negative and bigger than 1.");
        } else {
            var percentAsBigDecimal = BigDecimal.valueOf(percent);
            var valueToSubtract = value.multiply(percentAsBigDecimal);
            return Money.of(
                    value.subtract(valueToSubtract)
            );
        }
    }
}
