package com.example.screening.domain;

import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "uuid")
@ToString
class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "freeSeats"))
    private FreeSeats freeSeats;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "minAge"))
    private MinAge minAge;

    private Long filmId;

    boolean isAgeEnough(int age) {
        return age >= this.minAge.getValue();
    }

    void decreaseFreeSeatsByOne() {
        if (this.freeSeats.getValue() == 0) {
            throw new NoScreeningFreeSeatsException(this.id);
        } else {
            this.freeSeats = FreeSeats.of(this.freeSeats.getValue() - 1);
        }
    }

    boolean hasFreeSeats() {
        return this.freeSeats.getValue() > 0;
    }

    boolean canCancelReservation(Clock clock) {
        return Duration
                .between(LocalDateTime.now(clock), this.date.getValue())
                .toHours() > 24;
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.freeSeats.getValue())
                .minAge(this.minAge.getValue())
                .filmId(this.filmId)
                .build();
    }
}
