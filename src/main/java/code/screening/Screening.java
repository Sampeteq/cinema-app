package code.screening;

import code.screening.dto.ScreeningDTO;
import code.screening.exception.NoScreeningFreeSeatsException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@EqualsAndHashCode(of = "uuid")
@ToString
class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

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

    private UUID roomUuid;

    protected Screening() {
    }

    Screening(ScreeningDate date, FreeSeats freeSeats, MinAge minAge, Long filmId, UUID roomUuid) {
        this.date = date;
        this.freeSeats = freeSeats;
        this.minAge = minAge;
        this.filmId = filmId;
        this.roomUuid = roomUuid;
    }

    void decreaseFreeSeatsByOne() {
        if (this.freeSeats.getValue() == 0) {
            throw new NoScreeningFreeSeatsException(this.id);
        } else {
            this.freeSeats = FreeSeats.of(this.freeSeats.getValue() - 1);
        }
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.freeSeats.getValue())
                .minAge(this.minAge.getValue())
                .filmId(this.filmId)
                .roomUuid(this.roomUuid)
                .build();
    }
}
