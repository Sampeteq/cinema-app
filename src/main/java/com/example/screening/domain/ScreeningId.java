package com.example.screening.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
public class ScreeningId implements Serializable {

    private UUID value;
    static ScreeningId of(UUID value) {
        return new ScreeningId(value);
    }
}
