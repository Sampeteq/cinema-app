package com.example.film.domain;

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
public class FilmId implements Serializable {

    private UUID value;

    public static FilmId of(UUID value) {
        return new FilmId(value);
    }
}
